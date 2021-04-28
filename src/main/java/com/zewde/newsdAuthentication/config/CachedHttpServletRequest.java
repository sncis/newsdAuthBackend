package com.zewde.newsdAuthentication.config;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class CachedHttpServletRequest extends HttpServletRequestWrapper {
public byte[] cachedBody;

public CachedHttpServletRequest(HttpServletRequest request) throws IOException {
  super(request);
  InputStream requestInputStream = request.getInputStream();

  this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);

  }

  @Override
  public ServletInputStream getInputStream() throws  IOException{
    return new CachedServletInputStream(this.cachedBody);

  }

  @Override
  public BufferedReader getReader() throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((this.cachedBody));
    return new BufferedReader(new InputStreamReader(byteArrayInputStream));
  }

}
