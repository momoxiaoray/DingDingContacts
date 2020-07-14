package com.dmc.contacts;

import com.google.gson.Gson;

class DataUtil {

    public static GroupData getData() {
        Gson gson = new Gson();
        return gson.fromJson(testData(), GroupData.class);
    }


    private static String testData() {
        return "{\n" +
                "    \"goupName\":\"顶级部门\",\n" +
                "    \"goupId\":\"asdaskldalskd\",\n" +
                "    \"groupUsers\":[\n" +
                "        {\n" +
                "            \"userName\":\"测试人员1\",\n" +
                "            \"userPhone\":\"123123123123\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"userName\":\"测试人员2\",\n" +
                "            \"userPhone\":\"2222222\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"userName\":\"测试人员3\",\n" +
                "            \"userPhone\":\"23232323\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"groupChilds\":[\n" +
                "        {\n" +
                "            \"goupName\":\"2级部门1\",\n" +
                "            \"goupId\":\"asdaskldalskd\",\n" +
                "            \"groupUsers\":[\n" +
                "                {\n" +
                "                    \"userName\":\"测试人员1\",\n" +
                "                    \"userPhone\":\"123123123123\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"userName\":\"测试人员2\",\n" +
                "                    \"userPhone\":\"2222222\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"userName\":\"测试人员3\",\n" +
                "                    \"userPhone\":\"23232323\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"groupChilds\":[\n" +
                "                {\n" +
                "                    \"goupName\":\"3级部门1\",\n" +
                "                    \"goupId\":\"asdaskldalskd\",\n" +
                "                    \"groupUsers\":[\n" +
                "                        {\n" +
                "                            \"userName\":\"测试人员1\",\n" +
                "                            \"userPhone\":\"123123123123\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"userName\":\"测试人员2\",\n" +
                "                            \"userPhone\":\"2222222\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"userName\":\"测试人员3\",\n" +
                "                            \"userPhone\":\"23232323\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"goupName\":\"3级部门2\",\n" +
                "                    \"goupId\":\"asdaskldalskd\",\n" +
                "                    \"groupUsers\":[\n" +
                "                        {\n" +
                "                            \"userName\":\"测试人员1\",\n" +
                "                            \"userPhone\":\"123123123123\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"userName\":\"测试人员2\",\n" +
                "                            \"userPhone\":\"2222222\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"userName\":\"测试人员3\",\n" +
                "                            \"userPhone\":\"23232323\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"goupName\":\"2级部门2\",\n" +
                "            \"goupId\":\"asdaskldalskd\",\n" +
                "            \"groupUsers\":[\n" +
                "                {\n" +
                "                    \"userName\":\"测试人员1\",\n" +
                "                    \"userPhone\":\"123123123123\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"userName\":\"测试人员2\",\n" +
                "                    \"userPhone\":\"2222222\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"userName\":\"测试人员3\",\n" +
                "                    \"userPhone\":\"23232323\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"groupChilds\":[\n" +
                "\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
