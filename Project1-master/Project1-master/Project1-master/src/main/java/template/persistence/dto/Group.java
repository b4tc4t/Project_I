package template.persistence.dto;

public class Group {
    private String description;
    private String displayName;
    private String mailNickname;

    public Group(){}
    public Group(String description ,String displayName ,String mailNickname){
        this.description = description;
        this.displayName = displayName;
        this.mailNickname = mailNickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMailNickname() {
        return mailNickname;
    }

    public void setMailNickname(String mailNickname) {
        this.mailNickname = mailNickname;
    }
}
