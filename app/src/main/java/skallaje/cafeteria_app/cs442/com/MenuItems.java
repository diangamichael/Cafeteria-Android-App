package skallaje.cafeteria_app.cs442.com;

/**
 * Created by shara on 11/8/2015.
 */
public class MenuItems {
    private int id;
    private String item;
    private float price;
    private String desc;
    // private boolean checked;

    public MenuItems(int id, String item, float price, String desc) {
        this.id=id;
        this.item = item;
        this.price = price;
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }

    public int getID(){
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

  /*  public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    */
}
