package com.mvvm.opensoure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 初始化 {@link #onCreate(Context)}
 * </p>
 */
@SuppressWarnings("unused")
public class AppRootFileUtils {

    public static final int SIZE_TYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZE_TYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZE_TYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZE_TYPE_GB = 4;//获取文件大小单位为GB的double值
    /**
     * 配置文件名
     */
    public static String CONFIG_NAME = "common_share";
    /**
     * 配置文件名
     */
    public static String ROOT_PATH = "mvvm";
    private Context context;
    private SharedPreferences share;

    private AppRootFileUtils() {
    }

    public static void onCreate(Context context) {
        if (INSTANCE.SINGLETON.instance.context == null) {
            INSTANCE.SINGLETON.instance.context = context.getApplicationContext();
            INSTANCE.SINGLETON.instance.share = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        }
    }

    public static AppRootFileUtils get() {
        return INSTANCE.SINGLETON.instance;
    }

    /**
     * 创建文件夹
     */
    public static File createDir(String path) {
        if (TextUtils.isEmpty(path)) {
            LogUtils.e("createDir FAIL, path can not Null");
            return null;
        }
        File f = new File(path);
        try {
            // 获得文件对象
            if (!f.exists() && !f.isDirectory()) {
                // 如果路径不存在,则创建
                boolean result = f.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 创建文件
     *
     * @param fileAbsoluteName 文件全名
     */
    public static File createFile(String fileAbsoluteName) {
        if (TextUtils.isEmpty(fileAbsoluteName)) {
            // Log.e(TAG, "createDirectory FAIL, path can not Null");
            return null;
        }
        File file = null;
        try {
            file = new File(fileAbsoluteName);
            File path = new File(file.getParent());
            if (!path.exists()) {
                createDir(path.toString());
            }
            boolean result = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     */
    public static void deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                deleteFile(file.getAbsolutePath());
            } // 删除子目录
            else {
                deleteDirectory(file.getAbsolutePath());
            }
        }
        // 删除当前目录
        boolean result = dirFile.delete();
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static int deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                boolean isDeleted = file.delete();
                LogUtils.d("FileUtils.deleteFile() isDeleted: %s", isDeleted);
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        } else {
            return 1;
        }
        return 0;
    }

    /**
     * 读取文件
     *
     * @param src
     * @return
     */
    public static String readFromFile(File src) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new

                    FileReader(src));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e);
        }
        return FormatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e);
        }
        return FormatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            boolean result = file.createNewFile();
            LogUtils.e("创建文件结果 ：%s", result);
        }
        return size / 1048576;
    }

    /**
     * 获取指定文件夹
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File fileList[] = f.listFiles();
        for (File aFileList : fileList) {
            if (aFileList.isDirectory()) {
                size = size + getFileSizes(aFileList);
            } else {
                size = size + getFileSize(aFileList);
            }
        }
        return size;
    }

    /**
     *  * 转换文件大小
     *  * @param fileS
     *  * @return
     *  
     */
    private static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     *  * 转换文件大小,指定转换的类型
     *  * @param fileS 
     *  * @param sizeType 
     *  * @return
     *  
     */
    private static double FormatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZE_TYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZE_TYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZE_TYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZE_TYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 是否存在SD卡
     *
     * @return
     */
    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 获取app在sd卡跟目录下的路径
     */
    public String getAppRootPath() {
        String storagePath = share.getString(ConfigKey.RootPath.getConfigKey(), null);
        if (storagePath == null) {
            storagePath = getStoragePathList().get(0);
            storagePath = storagePath + "/" + ROOT_PATH;
            SharedPreferences.Editor shareEdit = share.edit();
            shareEdit.putString(ConfigKey.RootPath.getConfigKey(), storagePath);
            shareEdit.commit();
        }
        LogUtils.d(" AppRootPath: %s", storagePath);
        return storagePath;
    }

    /**
     * 获取所有sdcard更目录
     */
    public List<String> getStoragePathList() {
        List<String> mountedPaths = new ArrayList<>();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method[] methods = storageManager.getClass().getMethods();
            boolean hasGetVolumeListMethod = false;
            boolean hasGetVolumeStateMethod = false;
            for (Method method : methods) {
                if ("getVolumeList".equals(method.getName())) {
                    hasGetVolumeListMethod = true;
                }
                if ("getVolumeState".equals(method.getName())) {
                    hasGetVolumeStateMethod = true;
                }
            }
            if (hasGetVolumeListMethod && hasGetVolumeStateMethod) {
                Method getVolumeListMethod = storageManager.getClass().getMethod("getVolumeList");
                Method getVolumeStateMethod = storageManager.getClass().getMethod("getVolumeState", String.class);
                Object[] objects = (Object[]) getVolumeListMethod.invoke(storageManager);
                for (Object object : objects) {
                    Method method = object.getClass().getMethod("getPath");
                    String path = (String) method.invoke(object);
                    String state = (String) getVolumeStateMethod.invoke(storageManager, path);
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        mountedPaths.add(path);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mountedPaths.size() == 0) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            mountedPaths.add(path);
        }
        return mountedPaths;
    }

    /**
     * 获取缓存图片路径
     */
    public String getImgCachePath() {
        return getPath(SDPath.Image_Cache.path);
    }

    /**
     * 获取下载路径
     */
    public String getDownloadPath() {
        return getPath(SDPath.Download.path);
    }

    /**
     * 获取日志路径
     */
    public String getLogPath() {
        return getPath(SDPath.Log.path);
    }

    /**
     * 获取奔溃日志路径
     */
    public String getCrashLogPath() {
        return getPath(SDPath.CrashLog.path);
    }

    public String getQrimagePath() {
        return getPath(SDPath.QrImage.path);
    }

    private String getPath(String dirName) {
        String path;
        String rootPath = getAppRootPath();
        if (rootPath.endsWith("/")) {
            path = rootPath + dirName;
        } else {
            path = rootPath + "/" + dirName;
        }
        return path;
    }

    /**
     * 保存图片
     *
     * @param context
     * @param bmp
     * @return
     */
    public File saveImage(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(getAppRootPath(), SDPath.QrImage.path);
        if (!appDir.exists()) {
            boolean result = appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param file
     */
    public void saveImageFileToGallery(Context context, File file) {

        String fileName = System.currentTimeMillis() + ".jpg";
        // 其次把文件插入到系统图库
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bitmap != null) {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file); //out is your output file
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }

            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        } else {
            try {
                LogUtils.e("file path>>> %s", file.getAbsolutePath());
                LogUtils.e("ERROR!!! %s", getFileSize(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param bmp
     */
    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        if (bmp == null) {
            return;
        }
        File appDir = new File(getAppRootPath(), SDPath.QrImage.path);
        if (!appDir.exists()) {
            boolean result = appDir.mkdirs();
            LogUtils.e("创建文件结果 ：%s", result);

        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 其次把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, fileName, null);
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file); //out is your output file
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bmp.recycle();
            bmp = null;
            /*
            if (file != null) {
                FileUtils.deleteFile(file.getAbsolutePath());
            }*/
        }

        // 最后通知图库更新
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));

    }

    enum INSTANCE {
        SINGLETON;
        AppRootFileUtils instance = new AppRootFileUtils();
    }

    enum ConfigKey {
        /**
         * App在sd卡的根目录
         */
        RootPath("root_path"),
        /**
         * 启动广告图片地址
         */
        AdvertImgUrl("advert_img_url"),
        /**
         * app下载地址
         */
        AppDownUrl("app_download_url");


        private String configKey;

        ConfigKey(String configKey) {
            this.configKey = configKey;
        }

        /**
         * 获取对应的key
         */
        public String getConfigKey() {
            return configKey;
        }
    }

    /**
     * 文件存储目录
     */
    public enum SDPath {
        /**
         * 文件缓存目录
         */
        Image_Cache("cache/image"),

        /**
         * 下载目录
         */
        Download("download"),
        /**
         * 奔溃日志文件目录
         */
        CrashLog("crashLog"),
        /**
         * 普通异常日志文件目录
         */
        Log("log"),
        /**
         * 保存的二维码图片
         */
        QrImage("qrImage");

        String path;

        SDPath(String path) {
            this.path = path;
        }
    }
}
