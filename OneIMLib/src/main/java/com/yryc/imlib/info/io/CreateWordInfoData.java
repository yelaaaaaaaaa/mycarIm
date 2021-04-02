package com.yryc.imlib.info.io;


import com.yryc.imlib.info.WordDataInfo;

public class CreateWordInfoData {

    private Response response;

    private String status;

    public CreateWordInfoData(Response response, String status) {
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
        private WordDataInfo data;

        public WordDataInfo getData() {
            return data;
        }

        public void setData(WordDataInfo data) {
            this.data = data;
        }
    }
}
