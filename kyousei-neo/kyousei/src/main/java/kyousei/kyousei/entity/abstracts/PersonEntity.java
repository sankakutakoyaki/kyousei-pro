package kyousei.kyousei.entity.abstracts;

import java.sql.ResultSet;
import java.time.LocalDate;

import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class PersonEntity implements IEntity {

    protected int person_id;
    protected String last_name;
    protected String first_name;
    protected String full_name;
    protected String last_name_kana;
    protected String first_name_kana;
    protected String full_name_kana;
    protected String phone_number;
    protected String postal_code;
    protected String full_address;
    protected String email;
    protected int gender;
    protected int blood_type;
    protected LocalDate birthday = LocalDate.of(9999, 12, 31);
    protected String user_name;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.person_id = rs.getInt("person_id");
            this.last_name = rs.getString("last_name");
            this.first_name = rs.getString("first_name");
            this.full_name = rs.getString("full_name");
            this.last_name_kana = rs.getString("last_name_kana");
            this.first_name_kana = rs.getString("first_name_kana");
            this.full_name_kana = rs.getString("full_name_kana");
            this.phone_number = rs.getString("phone_number");
            this.postal_code = rs.getString("postal_code");
            this.full_address = rs.getString("full_address");
            this.email = rs.getString("email");
            this.gender = rs.getInt("gender");
            this.blood_type = rs.getInt("blood_type");
            this.birthday = rs.getDate("birthday").toLocalDate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        // sb.append("DECLARE @PersonTable TABLE (");
        // sb.append("editor NVARCHAR(255)");
        // sb.append(", state NVARCHAR(50)");
        // sb.append(", regist_date DATETIME2(7)");
        // sb.append(", person_id INT");
        // sb.append(", last_name NVARCHAR(255)");
        // sb.append(", first_name NVARCHAR(255)");
        // sb.append(", last_name_kana NVARCHAR(255)");
        // sb.append(", first_name_kana NVARCHAR(255)");
        // sb.append(", phone_number NVARCHAR(15)");
        // sb.append(", postal_code NVARCHAR(8)");
        // sb.append(", full_address NVARCHAR(255)");
        // sb.append(", email NVARCHAR(255)");
        // sb.append(", gender INT");
        // sb.append(", blood_type INT");
        // sb.append(", birthday DATE");
        // sb.append(");");

        sb.append("DECLARE @NEW_CODE int; SET @NEW_CODE = ISNULL(@MAX_CODE, 0) + 1;");
        // sb.append("DECLARE @ID_TBL TABLE (person_id int);");
        // sb.append("DECLARE @NEW_PERSON_ID int;");
        sb.append("INSERT INTO persons (");
        sb.append("last_name");
        sb.append(", first_name");
        sb.append(", last_name_kana");
        sb.append(", first_name_kana");
        sb.append(", phone_number");
        sb.append(", postal_code");
        sb.append(", full_address");
        sb.append(", email");
        sb.append(", gender");
        sb.append(", blood_type");
        sb.append(", birthday");
        sb.append(")");
        sb.append(logString("作成"));
        // sb.append(" OUTPUT");
        // sb.append("'" + this.getUser_name() + "', '作成', CURRENT_TIMESTAMP, INSERTED.person_id, INSERTED.last_name, INSERTED.first_name, INSERTED.last_name_kana, INSERTED.first_name_kana, INSERTED.phone_number");
        // sb.append(", INSERTED.postal_code, INSERTED.full_address, INSERTED.email, INSERTED.gender, INSERTED.blood_type, INSERTED.birthday");
        // sb.append(" INTO @PersonTable (");
        // sb.append("editor, state, regist_date, person_id, last_name, first_name, last_name_kana, first_name_kana, phone_number, postal_code, full_address, email, gender, blood_type, birthday");
        // sb.append(")");
        sb.append(" VALUES (");
        sb.append("'" + this.getLast_name() + "'");
        sb.append(", '" + this.getFirst_name() + "'");
        sb.append(", '" + this.getLast_name_kana() + "'");
        sb.append(", '" + this.getFirst_name_kana() + "'");
        sb.append(", '" + this.getPhone_number() + "'");
        sb.append(", '" + this.getPostal_code() + "'");
        sb.append(", '" + this.getFull_address() + "'");
        sb.append(", '" + this.getEmail() + "'");
        sb.append(", " + this.getGender());
        sb.append(", " + this.getBlood_type());
        if (this.getBirthday() == null) {
            this.setBirthday(LocalDate.of(9999, 12, 31));
        }
        sb.append(", '" + this.getBirthday() + "'");
        sb.append(");");
        // sb.append("SELECT @NEW_PERSON_ID = number;");
        sb.append("DECLARE @NEW_PERSON_ID int;SET @NEW_PERSON_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("INSERT INTO persons_log SELECT * FROM @PersonTable;");
        // sb.append("'user', 'test', @PersonTable.person_id, @PersonTable.last_name, @PersonTable.first_name, @PersonTable.last_name_kana, @PersonTable.first_name_kana, @PersonTable.phone_number");
        // sb.append(", @PersonTable.postal_code, @PersonTable.full_address, @PersonTable.email, @PersonTable.gender, @PersonTable.blood_type, @PersonTable.birthday");
        // sb.append(" INSERTED.person_id, INSERTED.last_name,  INSERTED.first_name, INSERTED.last_name_kana, INSERTED.first_name_kana, INSERTED.phone_number");
        // sb.append(", INSERTED.postal_code, INSERTED.full_address, INSERTED.email, INSERTED.gender, INSERTED.blood_type, INSERTED.birthday");
        // sb.append(");");
        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        // sb.append("DECLARE @PersonTable TABLE (");
        // sb.append(" old_last_name NVARCHAR(255)");
        // sb.append(", old_first_name NVARCHAR(255)");
        // sb.append(", old_last_name_kana NVARCHAR(255)");
        // sb.append(", old_first_name_kana NVARCHAR(255)");
        // sb.append(", old_phone_number NVARCHAR(15)");
        // sb.append(", old_postal_code NVARCHAR(8)");
        // sb.append(", old_full_address NVARCHAR(255)");
        // sb.append(", old_email NVARCHAR(255)");
        // sb.append(", old_gender INT");
        // sb.append(", old_blood_type INT");
        // sb.append(", old_birthday DATE");
        // sb.append(", last_name NVARCHAR(255)");
        // sb.append(", first_name NVARCHAR(255)");
        // sb.append(", last_name_kana NVARCHAR(255)");
        // sb.append(", first_name_kana NVARCHAR(255)");
        // sb.append(", phone_number NVARCHAR(15)");
        // sb.append(", postal_code NVARCHAR(8)");
        // sb.append(", full_address NVARCHAR(255)");
        // sb.append(", email NVARCHAR(255)");
        // sb.append(", gender INT");
        // sb.append(", blood_type INT");
        // sb.append(", birthday DATE");
        // sb.append(");");
        sb.append(logTable());
        sb.append("UPDATE persons SET");
        sb.append(" last_name = '" + this.getLast_name() + "'");
        sb.append(", first_name = '" + this.getFirst_name() + "'");
        sb.append(", last_name_kana = '" + this.getLast_name_kana() + "'");
        sb.append(", first_name_kana = '" + this.getFirst_name_kana() + "'");
        sb.append(", phone_number = '" + this.getPhone_number() + "'");
        sb.append(", postal_code = '" + this.getPostal_code() + "'");
        sb.append(", full_address = '" + this.getFull_address() + "'");
        sb.append(", email = '" + this.getEmail() + "'");
        sb.append(", gender = " + this.getGender());
        sb.append(", blood_type = " + this.getBlood_type());
        if (this.getBirthday() == null) {
            this.setBirthday(LocalDate.of(9999, 12, 31));
        }
        sb.append(", birthday = '" + this.getBirthday() + "'");
        sb.append(logString("更新"));
        // sb.append(" OUTPUT '" + this.getUser_name() + "', '更新', CURRENT_TIMESTAMP");
        // sb.append(", INSERTED.person_id, INSERTED.last_name,  INSERTED.first_name, INSERTED.last_name_kana, INSERTED.first_name_kana, INSERTED.phone_number");
        // sb.append(", INSERTED.postal_code, INSERTED.full_address, INSERTED.email, INSERTED.gender, INSERTED.blood_type, INSERTED.birthday");
        // sb.append(" INTO persons_log (");
        // sb.append("editor, state, regist_date, person_id, last_name, first_name, last_name_kana, first_name_kana, phone_number, postal_code, full_address, email, gender, blood_type, birthday");
        // sb.append(")");
        // sb.append(")VALUES(");
        // sb.append("INSERTED.last_name, INSERTED.first_name, INSERTED.last_name_kana, INSERTED.first_name_kana, INSERTED.phone_number");
        // sb.append(", INSERTED.postal_code, INSERTED.full_address, INSERTED.email, INSERTED.gender, INSERTED.blood_type, INSERTED.birthday");
        // sb.append("i.last_name, i.first_name, i.last_name_kana, i.first_name_kana, i.phone_number, i.postal_code, i.full_address, i.email, i.gender, i.blood_type, i.birthday");
        // sb.append(")");
        // sb.append(" OUTPUT");
        // sb.append(" DELETED.last_name as old_last_name, INSERTED.last_name");
        // sb.append(", DELETED.first_name as old_first_name, INSERTED.first_name");
        // sb.append(", DELETED.last_name_kana as old_last_name_kana, INSERTED.last_name_kana");
        // sb.append(", DELETED.first_name_kana as old_first_name_kana, INSERTED.first_name_kana");
        // sb.append(", DELETED.phone_number as old_phone_number, INSERTED.phone_number");
        // sb.append(", DELETED.postal_code as old_postal_code, INSERTED.postal_code");
        // sb.append(", DELETED.full_address as old_full_address, INSERTED.full_address");
        // sb.append(", DELETED.email as old_email, INSERTED.email");
        // sb.append(", DELETED.gender as old_gender, INSERTED.gender");
        // sb.append(", DELETED.blood_type as old_blood_type, INSERTED.blood_type");
        // sb.append(", DELETED.birthday as old_birthday, INSERTED.birthday");
        // sb.append(" INTO @PersonTable(");
        // sb.append(" old_last_name, last_name, old_first_name, first_name, old_last_name_kana, last_name_kana, old_first_name_kana, first_name_kana");
        // sb.append(", old_phone_number, phone_number, old_postal_code, postal_code, old_full_address, full_address, old_email, email, old_gender, gender, old_blood_type, blood_type, old_birthday, birthday");
        // sb.append(")");
        sb.append(" WHERE person_id = " + this.getPerson_id() + ";");
        // 変更履歴
        sb.append("INSERT INTO persons_log SELECT * FROM @PersonTable;");
        // sb.append("DECLARE @OLD nvarchar(255), @NEW nvarchar(255);");
        // sb.append("SELECT @OLD = old_last_name, @NEW = last_name FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'last_name', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_first_name, @NEW = first_name FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'first_name', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_last_name_kana, @NEW = last_name_kana FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'last_name_kana', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_first_name_kana, @NEW = first_name_kana FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'first_name_kana', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_phone_number, @NEW = phone_number FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'phone_number', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_postal_code, @NEW = postal_code FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'postal_code', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_full_address, @NEW = full_address FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'full_address', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_email, @NEW = full_email FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'email', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_gender, @NEW = gender FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'gender', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_blood_type, @NEW = blood_type FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'blood_type', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        // sb.append("SELECT @OLD = old_birthday, @NEW = birthday FROM @PersonTable;");
        // sb.append("IF @OLD != @NEW ");
        // sb.append("BEGIN INSERT INTO employee_history (editor, current_table, current_column, current_id, before_column, after_column, state)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'persons', 'birthday', " + this.getPerson_id() + ", @OLD, @NEW, '更新') END;");
        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @PersonTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", person_id INT");
        sb.append(", last_name NVARCHAR(255)");
        sb.append(", first_name NVARCHAR(255)");
        sb.append(", last_name_kana NVARCHAR(255)");
        sb.append(", first_name_kana NVARCHAR(255)");
        sb.append(", phone_number NVARCHAR(15)");
        sb.append(", postal_code NVARCHAR(8)");
        sb.append(", full_address NVARCHAR(255)");
        sb.append(", email NVARCHAR(255)");
        sb.append(", gender INT");
        sb.append(", blood_type INT");
        sb.append(", birthday DATE");
        sb.append(");");
        return sb.toString();
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append("'" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.person_id");
        sb.append(", INSERTED.last_name");
        sb.append(", INSERTED.first_name");
        sb.append(", INSERTED.last_name_kana");
        sb.append(", INSERTED.first_name_kana");
        sb.append(", INSERTED.phone_number");
        sb.append(", INSERTED.postal_code");
        sb.append(", INSERTED.full_address");
        sb.append(", INSERTED.email");
        sb.append(", INSERTED.gender");
        sb.append(", INSERTED.blood_type");
        sb.append(", INSERTED.birthday");
        sb.append(" INTO @PersonTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", person_id");
        sb.append(", last_name");
        sb.append(", first_name");
        sb.append(", last_name_kana");
        sb.append(", first_name_kana");
        sb.append(", phone_number");
        sb.append(", postal_code");
        sb.append(", full_address");
        sb.append(", email");
        sb.append(", gender");
        sb.append(", blood_type");
        sb.append(", birthday");
        sb.append(")");
        return sb.toString();
    }
}
