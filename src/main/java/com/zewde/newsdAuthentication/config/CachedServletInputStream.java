package com.zewde.newsdAuthentication.config;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedServletInputStream extends ServletInputStream {

  private InputStream cachedInputStream;

  public CachedServletInputStream(byte[] cachedBody) {
    this.cachedInputStream = new ByteArrayInputStream(cachedBody);
  }

  @Override
  public boolean isFinished() {
    try {
      return cachedInputStream.available() == 0;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public void setReadListener(ReadListener readListener) {

  }

  @Override
  public int read() throws IOException {
    return  cachedInputStream.read();
  }
}
