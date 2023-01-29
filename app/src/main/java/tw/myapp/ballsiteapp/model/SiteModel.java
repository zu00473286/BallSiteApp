package tw.myapp.ballsiteapp.model;

public class SiteModel {

    private int image;

    private String siteID;

    private String price;

    public SiteModel(int image, String siteID, String price) {
        this.image = image;
        this.siteID = siteID;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
