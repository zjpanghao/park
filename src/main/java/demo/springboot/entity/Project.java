package demo.springboot.entity;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "project")
public class Project {
    @Id
    private Integer id;

    @Column(name = "root_dir")
    private String rootDir;

    private String name;

    private String introduction;

    @Column(name = "filter")
    private String filter;

    private String data;

    @Column(name = "serv_ip")
    private String servIp;

    @ManyToOne
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProjectDir() {
        return rootDir + File.separator + name;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getServIp() {
        return servIp;
    }

    public void setServIp(String servIp) {
        this.servIp = servIp;
    }

    public String getFilterFullDir() {
        return getProjectDir() + File.separator + filter;
    }

    public String getDataFullDir() {
        return getProjectDir() + File.separator + data;
    }
}
