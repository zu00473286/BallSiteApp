package tw.myapp.ballsiteapp.model;

public class SiteModel {

    private String siteID;

    private String price;

    private String site_id;

    public SiteModel(String siteID, String price, String site_id) {
        this.siteID = siteID;
        this.price = price;
        this.site_id = site_id;
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

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

}
