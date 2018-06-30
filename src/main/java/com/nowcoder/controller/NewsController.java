package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.xml.SqlXmlObjectMappingHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;


    private  static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
//            String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }
   @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
   @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        }catch (Exception e){
                logger.error("读取图片错误" + e.getMessage());
        }
    }
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        return newsService.addNews(image,title,link);
    }

    @RequestMapping(path = {"/news/{newsId}"})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.selectById(newsId);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        if(news != null){
            //点赞状态
            if(localUserId != 0){
                model.addAttribute("like", likeService.getLikeStatus(localUserId,EntityType.ENTITY_NEWS,news.getId()));
            }else{
                model.addAttribute("like",0);
            }
            //评论
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(),EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment",comment);
                vo.set("user",userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments",commentVOs);
        }
        User user = userService.getUser(news.getUserId());
        model.addAttribute("owner",user);
        model.addAttribute("news",news);
        return "detail";
    }
    @RequestMapping(path = "/addComment",method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setStatus(0);
            commentService.addComment(comment);
            //更新news里的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
            //怎么异步化

        }catch (Exception e){
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
}
