package com.yryc.imlib.model.chat;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.yryc.imlib.info.GeneralMessageDataInfo;
import com.yryc.imlib.info.GroupMessageDataInfo;

import java.util.HashMap;
import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/3/22 11:12
 * @describe : 富文本
 */


public class ChatRich extends ChatBody {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body {
        private String type;
        private String tip;
        private String url;
        private Image image;
        @SerializedName("action_url")
        private String actionUrl;
        @SerializedName("action_type")
        private int actionType;
        @SerializedName("action_param")
        private HashMap<String, String> actionParam;
        private List<Content> header;
        private List<Content> body;
        private List<Content> footer;
        @SerializedName("button_orientation")
        private int buttonOrientation;
        private List<Button> buttons;
        private Button result;

        public int getButtonOrientation() {
            return buttonOrientation;
        }

        public void setButtonOrientation(int buttonOrientation) {
            this.buttonOrientation = buttonOrientation;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public String getActionUrl() {
            return actionUrl;
        }

        public void setActionUrl(String actionUrl) {
            this.actionUrl = actionUrl;
        }

        public int getActionType() {
            return actionType;
        }

        public void setActionType(int actionType) {
            this.actionType = actionType;
        }

        public HashMap<String, String> getActionParam() {
            return actionParam;
        }

        public void setActionParam(HashMap<String, String> actionParam) {
            this.actionParam = actionParam;
        }

        public List<Content> getHeader() {
            return header;
        }

        public void setHeader(List<Content> header) {
            this.header = header;
        }

        public List<Content> getBody() {
            return body;
        }

        public void setBody(List<Content> body) {
            this.body = body;
        }

        public List<Content> getFooter() {
            return footer;
        }

        public void setFooter(List<Content> footer) {
            this.footer = footer;
        }

        public List<Button> getButtons() {
            return buttons;
        }

        public void setButtons(List<Button> buttons) {
            this.buttons = buttons;
        }

        public Button getResult() {
            return result;
        }

        public void setResult(Button result) {
            this.result = result;
        }

        public static class Image {
            @SerializedName("image_url")
            private String imageUrl;
            @SerializedName("image_position")
            private int imagePosition;
            @SerializedName("image_height")
            private String imageHeight;
            @SerializedName("image_width")
            private String imageWidth;
            @SerializedName("image_title")
            private String imageTitle;

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public int getImagePosition() {
                return imagePosition;
            }

            public void setImagePosition(int imagePosition) {
                this.imagePosition = imagePosition;
            }

            public String getImageHeight() {
                return imageHeight;
            }

            public void setImageHeight(String imageHeight) {
                this.imageHeight = imageHeight;
            }

            public String getImageWidth() {
                return imageWidth;
            }

            public void setImageWidth(String imageWidth) {
                this.imageWidth = imageWidth;
            }

            public String getImageTitle() {
                return imageTitle;
            }

            public void setImageTitle(String imageTitle) {
                this.imageTitle = imageTitle;
            }
        }

        public static class Content {
            private String content;
            private int align;
            private List<Style> style;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getAlign() {
                return align;
            }

            public void setAlign(int align) {
                this.align = align;
            }

            public List<Style> getStyle() {
                return style;
            }

            public void setStyle(List<Style> style) {
                this.style = style;
            }
        }

        public static class Style {
            private String color;
            private int size;
            private boolean bold;
            private int loc;
            private int len;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public boolean isBold() {
                return bold;
            }

            public void setBold(boolean bold) {
                this.bold = bold;
            }

            public int getLoc() {
                return loc;
            }

            public void setLoc(int loc) {
                this.loc = loc;
            }

            public int getLen() {
                return len;
            }

            public void setLen(int len) {
                this.len = len;
            }
        }

        public static class Button {
            private String content;
            private int align;
            private List<Style> style;
            @SerializedName("action_url")
            private String actionUrl;
            @SerializedName("action_type")
            private int actionType;
            @SerializedName("action_result")
            private Content actionResult;
            @SerializedName("action_param")
            private HashMap<String, String> actionParam;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getAlign() {
                return align;
            }

            public void setAlign(int align) {
                this.align = align;
            }

            public List<Style> getStyle() {
                return style;
            }

            public void setStyle(List<Style> style) {
                this.style = style;
            }

            public String getActionUrl() {
                return actionUrl;
            }

            public void setActionUrl(String actionUrl) {
                this.actionUrl = actionUrl;
            }

            public int getActionType() {
                return actionType;
            }

            public void setActionType(int actionType) {
                this.actionType = actionType;
            }

            public Content getActionResult() {
                return actionResult;
            }

            public void setActionResult(Content actionResult) {
                this.actionResult = actionResult;
            }

            public HashMap<String, String> getActionParam() {
                return actionParam;
            }

            public void setActionParam(HashMap<String, String> actionParam) {
                this.actionParam = actionParam;
            }
        }

    }

    public static class Builder {
        private GeneralMessageDataInfo generalMessageDataInfo;
        private GroupMessageDataInfo groupMessageDataInfo;
        private boolean isGroup;

        public Builder(GeneralMessageDataInfo generalMessageDataInfo) {
            this.generalMessageDataInfo = generalMessageDataInfo;
            isGroup = false;
        }

        public Builder(GroupMessageDataInfo groupMessageDataInfo) {
            this.groupMessageDataInfo = groupMessageDataInfo;
            isGroup = true;
        }

        public ChatRich build() {
            ChatRich chatRich;
            if (isGroup) {
                chatRich = new ChatBody.Builder(groupMessageDataInfo).build(new ChatRich());
            } else {
                chatRich = new ChatBody.Builder(generalMessageDataInfo).build(new ChatRich());
            }
            chatRich.body = new Gson().fromJson(chatRich.json, ChatRich.Body.class);
            return chatRich;
        }
    }

}
