package com.geyan.util;

import java.io.Serializable;

public class Privacy implements Serializable {
  public String text;
  public String url;
  public Privacy(String t, String u) {
    text = t;
    url = u;
  }
}
