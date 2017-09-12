package co.getchannel.channel.models.internal;

/**
 * Created by rataphon on 8/25/2017 AD.
 */

public class Message {
    private String text;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setImageData(String imageUrl) {
        Data d = new Data();
        d.setCard(d.getImageCard(imageUrl));
        this.data = d;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    private class Data{

        private Card card;

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public Card getImageCard(String imageUrl) {
            Card c = new Card();
            c.setType("image");
            c.setPayload(c.getPayloadImage(imageUrl));
            return c;
        }

        private class Card{
            private String type;
            private Payload payload;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Payload getPayload() {
                return payload;
            }

            public void setPayload(Payload payload) {
                this.payload = payload;
            }

            public Payload getPayloadImage(String imageUrl) {
                Payload p = new Payload();
                p.setUrl(imageUrl);
                return p;
            }

            private class Payload{
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }

}
