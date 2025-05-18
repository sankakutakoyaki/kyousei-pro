package kyousei.kyousei.entity.data;

import java.sql.ResultSet;

import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class IdEntity implements IEntity{
    private int id;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
