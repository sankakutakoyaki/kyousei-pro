package kyousei.kyousei.entity.data;

import java.sql.ResultSet;

import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class SidebarData implements IEntity {
    private int id;
    private int parent_id;
    private String text;
    private String img;
    private String func = "onclick=\"clickSidebarItem(this)\"";

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.parent_id = rs.getInt("parent_id");
            this.text = rs.getString("text");
            this.img = rs.getString("img");
            // this.func = "onclick=\"clickSidebarItem(this)\"";   
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
