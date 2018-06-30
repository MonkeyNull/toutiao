package com.nowcoder.service;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class NewsService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NewsService.class);
    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    HostHolder hostHolder;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotpos = file.getOriginalFilename().lastIndexOf(".");
        if (dotpos < 0) {
            return null;
        }
//      将后缀名拿出来，转换成小写
        String fileExt = file.getOriginalFilename().substring(dotpos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + fileExt;
        //这个方法可以把file里的数据拷贝到数据中。
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + filename;
    }
    public String addNews(String image,String title,String link){
        if(StringUtils.isBlank(image) || StringUtils.isBlank(title) || StringUtils.isBlank(link)){
            return ToutiaoUtil.getJSONString(1,"字段不能为空");
        }
        try{
        News news = new News();
        if(hostHolder.getUser() != null){
            news.setUserId(hostHolder.getUser().getId());
        }else {
            //默认id：3为匿名用户
            news.setUserId(3);
        }
        news.setImage(image);
        news.setCommentCount(0);
        news.setCreatedDate(new Date());
        news.setLink(link);
        news.setTitle(title);
        newsDAO.addNews(news);
        return ToutiaoUtil.getJSONString(0,"发布成功");
        }catch (Exception e){
            logger.error("添加资讯错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }
    public News selectById(int newsId){
       return newsDAO.selectNewsById(newsId);
    }
    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }
    public int updateLikeCount(int id, int count){
        return newsDAO.updateLikeCount(id,count);
    }



}
