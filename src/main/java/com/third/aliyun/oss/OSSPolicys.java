package com.third.aliyun.oss;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class OSSPolicys {
    private static final String READ_POLICY       = "{                                                                     "
                                                          + "  \"Statement\": [                                                    "
                                                          + "    {                                                                 "
                                                          + "      \"Action\": [                                                   "
                                                          + "        \"oss:GetObject\",                                            "
                                                          + "        \"oss:ListObjects\"                                           "
                                                          + "      ],                                                              "
                                                          + "      \"Effect\": \"Allow\",                                          "
                                                          + "      \"Resource\": [\"acs:oss:*:*:%s/*\", \"acs:oss:*:*:%s\"]      "
                                                          + "    }                                                                 "
                                                          + "  ],                                                                  "
                                                          + "  \"Version\": \"1\"                                                  "
                                                          + "}                                                                     ";
    private static final String READ_WRITE_POLICY = "{                                                                                           "
                                                          + "  \"Statement\": [                                                                          "
                                                          + "    {                                                                                       "
                                                          + "      \"Action\": [                                                                         "
                                                          + "        \"oss:GetObject\",                                                                  "
                                                          + "        \"oss:PutObject\",                                                                  "
                                                          + "        \"oss:DeleteObject\",                                                               "
                                                          + "        \"oss:ListParts\",                                                                  "
                                                          + "        \"oss:AbortMultipartUpload\",                                                       "
                                                          + "        \"oss:ListObjects\"                                                                 "
                                                          + "      ],                                                                                    "
                                                          + "      \"Effect\": \"Allow\",                                                                "
                                                          + "      \"Resource\": [\"acs:oss:*:*:%s/*\", \"acs:oss:*:*:%s\"]          "
                                                          + "    }                                                                                       "
                                                          + "  ],                                                                                        "
                                                          + "  \"Version\": \"1\"                                                                        "
                                                          + "}  ";
    private static final String ALL_POLICY        = "{                                          "
                                                          + "  \"Statement\": [                         "
                                                          + "    {                                      "
                                                          + "      \"Action\": [                        "
                                                          + "        \"oss:*\"                          "
                                                          + "      ],                                   "
                                                          + "      \"Effect\": \"Allow\",               "
                                                          + "      \"Resource\": [\"acs:oss:*:*:*\"]    "
                                                          + "    }                                      "
                                                          + "  ],                                       "
                                                          + "  \"Version\": \"1\"                       "
                                                          + "}                                          ";

    public static String readPolicy(String bucket) {
        return String.format(READ_POLICY, bucket, bucket);
    }

    public static String readWritePolicy(String bucket) {
        return String.format(READ_WRITE_POLICY, bucket, bucket);
    }

    public static String allPolicy() {
        return ALL_POLICY;
    }

}
