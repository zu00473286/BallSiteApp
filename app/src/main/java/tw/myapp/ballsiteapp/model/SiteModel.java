package tw.myapp.ballsiteapp.model;

public class SiteModel {

    private String siteID;

    private String price;

    public SiteModel(String siteID, String price) {
        this.siteID = siteID;
        this.price = price;
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
