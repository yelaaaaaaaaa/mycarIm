package com.yryc.imlib.info.io;

public class GetUserInformationDataInfo {

    private Response response;

    private String status;

    public GetUserInformationDataInfo(Response response, String status) {
        this.response = response;
        this.status = status;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }


    public class Response {
        private PersonalInfoDataInfo data;

        public void setData(PersonalInfoDataInfo data) {
            this.data = data;
        }

        public PersonalInfoDataInfo getData() {
            return this.data;
        }

    }
}
