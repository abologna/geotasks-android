package com.geotasks.util;

import android.text.*;

public class StringUtil
{
  public static Option use(String object)
  {
    return new Option(object);
  }
  public static class Option
  {
    private final String string;
    public Option(String string)
    {
      this.string = string;
    }

    public String orDefault(String _default)
    {
      return TextUtils.isEmpty(string)? _default : string;
    }
  }
}
