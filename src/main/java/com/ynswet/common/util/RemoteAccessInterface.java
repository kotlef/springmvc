package com.ynswet.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 *     
 * 项目名称：LXICBP-Common    
 * 类名称：RemoteAccessInterface    
 * 类描述：跨域远程访问接口    
 * 创建人：root    
 * 创建时间：2013-10-30 上午11:23:12    
 * 修改人：root    
 * 修改时间：2013-10-30 上午11:23:12    
 * 修改备注：    
 * @version     
 *
 */
public class RemoteAccessInterface
{
    /**
       
     * remoteURL(访问远程地址)    
       
     * TODO(这里描述这个方法适用条件 – 可选)    
       
     * TODO(这里描述这个方法的执行流程 – 可选)    
       
     * TODO(这里描述这个方法的使用方法 – 可选)    
       
     * TODO(这里描述这个方法的注意事项 – 可选)    
       
     * @param   name    
       
     * @param  @return    设定文件    
       
     * @return String    DOM对象    
       
     * @Exception 异常对象    
       
     * @since  CodingExample　Ver(编码范例查看) 1.1
     */
    static public String remoteURL(String url)
    {
        URL urls = null;
        try
        {
            urls = new URL(url);
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }
        BufferedReader r = null;
        try
        {
            URLConnection con = urls.openConnection();
            r = new BufferedReader( new InputStreamReader( con.getInputStream(), "UTF-8" ) );
        }
        catch ( UnsupportedEncodingException e1 )
        {
            e1.printStackTrace();
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }

        String str = "";

        try
        {
            str = r.readLine();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        try
        {
            r.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return str;
    }

}
