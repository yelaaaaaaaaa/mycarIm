package com.yryc.imlib.info.io;



import com.yryc.imlib.info.WordDataInfo;

import java.util.List;

public class GetWordInfoList {

    private Response response;

    private String status;

    public GetWordInfoList(Response response, String status) {
        this.response = response;
        this.status = status;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Response {
        private List<WordDataInfo> data;

        public void setData(List<WordDataInfo> data) {
            this.data = data;
        }

        public List<WordDataInfo> getData() {
            return data;
        }
    }
}
