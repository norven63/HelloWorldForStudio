package com.helloguava;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    @butterknife.Bind(R.id.log_textView)
    TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butterknife.ButterKnife.bind(this);

        StringBuffer logBuffer = new StringBuffer();
        Date start = new Date();

        start = new Date();
        copyFileUseNioMapped(Environment.getExternalStorageDirectory().getPath() + "/test.jpg", Environment.getExternalStorageDirectory().getPath() + "/nio.jpg");
        logBuffer.append("nio: " + (new Date().getTime() - start.getTime()) + "\n");

        start = new Date();
        copyFileUseBufferedStream(Environment.getExternalStorageDirectory().getPath() + "/test.jpg", Environment.getExternalStorageDirectory().getPath() + "/buffer.jpg");
        logBuffer.append("buffer: " + (new Date().getTime() - start.getTime()) + "\n");

        try {
            Files.copy(new File(Environment.getExternalStorageDirectory().getPath() + "/test.jpg"), new File(Environment.getExternalStorageDirectory().getPath() + "/guava.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logBuffer.append("guava: " + (new Date().getTime() - start.getTime()) + "\n");


    }

    public static boolean copyFileUseNioMapped(final String resource, final String destination) {
        boolean result = false;
        RandomAccessFile rafi = null;
        RandomAccessFile rafo = null;
        FileChannel readChannel = null;// 读文件通道
        FileChannel writeChannel = null;// 写文件通道
        try {
            rafi = new RandomAccessFile(resource, "r");
            rafo = new RandomAccessFile(destination, "rw");
            readChannel = rafi.getChannel();// 读文件通道
            writeChannel = rafo.getChannel();// 写文件通道
            long fileSize = readChannel.size();
            MappedByteBuffer mbbi = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            MappedByteBuffer mbbo = writeChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

            byte tmpBuf[] = new byte[1024 * 8];
            while (mbbi.hasRemaining()) {
                ByteBuffer tmpByteBuffer = mbbi.get(tmpBuf);
                mbbo.put(tmpByteBuffer);
            }

            result = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        } finally {
            //
            if (readChannel != null) {
                try {
                    readChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    readChannel = null;
                }
            }

            //
            if (writeChannel != null) {
                try {
                    writeChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    writeChannel = null;
                }
            }
            //
            if (rafi != null) {
                try {
                    rafi.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    rafi = null;
                }
            }
            //
            if (rafo != null) {
                try {
                    rafo.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    rafo = null;
                }
            }
        }

        return result;
    }

    /**
     * 使用流的方式拷贝文件
     *
     * @param resource
     * @param destination
     */
    public static boolean copyFileUseBufferedStream(final String resource, final String destination) {
        boolean result = false;
        InputStream is = null;
        OutputStream os = null;
        try {

            // TODO : 一定要对 输入流和输出流都使用Buffered装饰, 性能才是最优的
            // TODO : 使用默认的buffer大小, 不一定是最优的, 可以指定缓存区大小
            is = new BufferedInputStream(new FileInputStream(resource), 1024 * 8);
            os = new BufferedOutputStream(new FileOutputStream(destination), 1024 * 8);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                os.write(buffer, 0, length);
            }

            os.flush();

            result = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        } finally {
            //
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }
            //
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    os = null;
                }
            }
        }

        return result;
    }
}
