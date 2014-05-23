/*
 * Copyright (C) 2009-2013 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.common.android.utilities;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang3.CharEncoding;

import android.content.Context;
import android.net.Uri;
import fi.iki.elonen.SimpleWebServer;

public class UrlUtils {

  private static String SCHEME_HTTP = "http";

  public static Uri getWebViewContentUri(Context c) {
    return Uri.parse(SCHEME_HTTP + "://" + SimpleWebServer.HOSTNAME + ":"
        + Integer.toString(SimpleWebServer.PORT) + "/");
  }

  /**
   * The constructed URI may be invalid if it references a file that is in a
   * legacy directory or an inaccessible directory.
   *
   * Typical usage:
   *
   * File file;
   *
   * getAsWebViewUri(this, appName, ODKFileUtils.asUriFragment(appName, file));
   *
   * @param context
   * @param appName
   * @param uriFragment
   * @return
   */
  public static String getAsWebViewUri(Context context, String appName, String uriFragment) {
    Uri u = UrlUtils.getWebViewContentUri(context);
    // we need to escape the segments.
    u = Uri.withAppendedPath(u, Uri.encode(appName));

    String pathPart;
    String queryPart;
    String hashPart;
    int idxQ = uriFragment.indexOf("?");
    int idxH = uriFragment.indexOf("#");
    if ( idxQ != -1 ) {
      if ( idxH != -1 ) {
        if ( idxH < idxQ ) {
          pathPart = uriFragment.substring(0,idxH);
          queryPart = "";
          hashPart = uriFragment.substring(idxH);
        } else {
          pathPart = uriFragment.substring(0,idxQ);
          queryPart = uriFragment.substring(idxQ, idxH);
          hashPart = uriFragment.substring(idxH);
        }
      } else {
        pathPart = uriFragment.substring(0,idxQ);
        queryPart = uriFragment.substring(idxQ);
        hashPart = "";
      }
    } else if ( idxH != -1 ) {
      pathPart = uriFragment.substring(0,idxH);
      queryPart = "";
      hashPart = uriFragment.substring(idxH);
    } else {
      pathPart = uriFragment;
      queryPart = "";
      hashPart = "";
    }

    String[] segments = pathPart.split("/");
    for (String s : segments) {
      u = Uri.withAppendedPath(u, Uri.encode(s));
    }
    return u.toString() + queryPart + hashPart;
  }


  public static boolean isValidUrl(String url) {

    try {
      new URL(URLDecoder.decode(url, CharEncoding.UTF_8));
      return true;
    } catch (MalformedURLException e) {
      return false;
    } catch (UnsupportedEncodingException e) {
      return false;
    }

  }

}