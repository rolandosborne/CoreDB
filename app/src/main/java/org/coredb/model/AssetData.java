package org.coredb.model;

import org.springframework.core.io.InputStreamResource;

public class AssetData {
    private InputStreamResource resource;
    private String transform;
    private Long begin;
    private Long end;
    private Long size;

    public AssetData(InputStreamResource r, String t, Long s) {
      resource = r;
      transform = t;
      size = s;
      begin = (long)0;
      end = s - (long)1;
    }

    public AssetData(InputStreamResource r, String t, Long b, Long e, Long s) {
      resource = r;
      transform = t;
      size = s;
      begin = b;
      end = e;
    }

    public InputStreamResource getResource() {
      return resource;
    }

    public Long getBegin() {
      return this.begin;
    }
  
    public Long getEnd() {
      return this.end;
    }

    public Long getSize() {
      return this.size;
    }

    public String getTransform() {
      return transform;
    }

    public String getContentType() {
      if(transform != null) {
        if(transform.equals("P01") || transform.equals("P02") || transform.equals("P03") || transform.equals("P04") ||
              transform.equals("P05") || transform.equals("P06")) {
          return "image/jpg";
        }
        if(transform.startsWith("F01")) {
          return "image/jpg";
        }
        if(transform.equals("A01") || transform.equals("A02") || transform.equals("A03") || transform.equals("A04")) {
          return "audio/mpeg";
        }
        if(transform.equals("V01") || transform.equals("V02") || transform.equals("V03") || transform.equals("V04")) {
          return "video/mp4";
        }
      }
      return "application/octet_stream";
    }
  }

