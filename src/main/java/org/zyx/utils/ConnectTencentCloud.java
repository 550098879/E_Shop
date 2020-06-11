package org.zyx.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;


/** 腾讯云服务器工具类
 * Created by SunShine on 2020/5/14.
 */
@Component
public class ConnectTencentCloud {

    private static COSClient cosClient = null;
    private String SecretId = "AKIDM1TnJaOWBbrrdCrbThfYJwLTM0NTrMvG";
    private String SecretKey = "K1SyQbWKsLlxwNYVGwMfjurY0LUegSy8";
    private String apCity = "ap-guangzhou";//存储桶地域
    private static String  bucketName = "01-start-1302118368";//存储桶名称

    public org.slf4j.Logger log = LoggerFactory.getLogger(ConnectTencentCloud.class);


    //构造方法创建腾讯云连接
    public ConnectTencentCloud(){

        //1.初始化用户身份信息
        COSCredentials cred = new BasicCOSCredentials(SecretId,SecretKey);
        //2.设置bucket的区域,COS地域的简称参考
        Region region = new Region(apCity);
        //3.ClientConfig中包含了设置region,https(默认http),超时,代理等set方法
        ClientConfig clientConfig = new ClientConfig(region);
        //4.生成cos客户端
        cosClient = new COSClient(cred,clientConfig);


    }

    public static void closeClient(){
        if(cosClient != null){
            cosClient.shutdown();//关闭 cosClient，并释放 HTTP 连接的后台管理线程
        }
    }


    /**
     * @describe 上传文件的方法
     * @methods uploadObject 方法名
     * @parameter  fileUrl 上传文件地址
     * @parameter  fileKey 文件对象名称
     * @parameter @return 对象列表
     */
    public PutObjectResult uploadObject(String fileUrl, String fileKey) {
        try {
            // 指定要上传的文件
            File localFile = new File(fileUrl);
            // fileKey 指定要上传到 COS 上对象键
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, localFile);
            PutObjectResult  putObjectResult = cosClient.putObject(putObjectRequest);
            return putObjectResult;
        } catch (CosServiceException serverException) {
            log.error(serverException.getErrorMessage());
            throw  new RuntimeException("上传文件平台Server异常"+serverException.getErrorMessage());
        } catch (CosClientException clientException) {
            log.error(clientException.getMessage());
            throw  new RuntimeException("上传文件平台Client异常"+clientException.getMessage());
        }

    }

    /**
     * @describe 上传文件的方法
     * @methods uploadObject 方法名
     * @parameter  fileUrl 上传文件地址
     * @parameter  fileKey 文件对象名称
     * @parameter @return 对象列表
     */
    public PutObjectResult uploadStream(InputStream inputStream, String fileKey,long size) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
// 设置输入流长度为500
        objectMetadata.setContentLength(size);
// 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType("application/octet-stream");
        PutObjectResult  putObjectResult = cosClient.putObject(bucketName, fileKey, inputStream, objectMetadata);
        String etag = putObjectResult.getETag();
        inputStream.close();
        return putObjectResult;
    }


    /**
     * 查询存储桶列表 已经测试、成功
     *
     * @return
     */
    public List<Bucket> queryBucketList() {
        List<Bucket> buckets = null;
        try {
            buckets = cosClient.listBuckets();
            System.out.println(buckets);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();

        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
        return buckets;
    }
    /**
     * 下载对象、测试成功
     */
    public void downLoadObject(String key,String fileName,String path) {
        try {
            // 指定对象在 COS 上的对象键
            // 指定要下载到的本地路径
            File downFile = new File(path+fileName);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    /**
     * 删除对象 测试成功
     */
    public void deleteObjectRequest(String key) {
        try {
            cosClient.deleteObject(bucketName, key);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }


    /**使用永久密钥生成一个带签名的下载链接
     * @param key
     * @return
     */
    public String getUrl(String key){

        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
// 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(1小时)
// 这里设置签名在半个小时后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
        req.setExpiration(expirationDate);
        URL url = cosClient.generatePresignedUrl(req);
        return url.toString();
    }







//    public static void main(String[] args) {
//        ConnectTencentCloud cloud = new ConnectTencentCloud();
//        1.上传测试
//        String fileUrl= "D:\\浏览器下载\\Java文件夹\\图片素材\\彼岸\\2233.jpg";
//        PutObjectResult putObjectResult = cloud.uploadObject(fileUrl, "2233娘");//路径,文件名
//        2.查询存储桶
//        List<Bucket> bucketList = cloud.queryBucketList();
//        3.文件下载
//        cloud.downLoadObject("2233娘","112233.png");
//        4.删除文件
//        cloud.deleteObjectRequest("2233娘");
//        cloud.closeClient();
//    }


}
