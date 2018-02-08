package com.pzp.manage.design.cor;


import org.springframework.util.StringUtils;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/8 14:52 星期四
 */
public class LeaveRequest {

    private String name;

    private String reason;

    private int days;

    private String groupLeaderInfo;

    private String managerInfo;

    private String departmentHeaderInfo;

    private String customInfo;

    private LeaveRequest(RequestBuilder builder) {
        this.name = builder.name;
        this.reason = builder.reason;
        this.days = builder.days;
        this.groupLeaderInfo = builder.groupLeaderInfo;
        this.managerInfo = builder.managerInfo;
        this.departmentHeaderInfo = builder.departmentHeaderInfo;
        this.customInfo = builder.customInfo;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public int getDays() {
        return days;
    }

    public String getGroupLeaderInfo() {
        return groupLeaderInfo;
    }

    public String getManagerInfo() {
        return managerInfo;
    }

    public String getDepartmentHeaderInfo() {
        return departmentHeaderInfo;
    }

    public String getCustomInfo() {
        return customInfo;
    }


    public static class RequestBuilder {

        private String name;
        private String reason;
        private int days;

        private String groupLeaderInfo;
        private String managerInfo;
        private String departmentHeaderInfo;
        private String customInfo;

        public RequestBuilder(String name, String reason, int days){
            this.name = name;
            this.reason = reason;
            this.days = days;
        }

        public RequestBuilder(LeaveRequest request){
            this.name = request.getName();
            this.reason = request.getReason();
            this.days = request.getDays();
            if(!StringUtils.isEmpty(request.getGroupLeaderInfo())){
                this.groupLeaderInfo = request.getGroupLeaderInfo();
            }
            if(!StringUtils.isEmpty(request.getManagerInfo())){
                this.managerInfo = request.getManagerInfo();
            }
            if(!StringUtils.isEmpty(request.getDepartmentHeaderInfo())){
                this.departmentHeaderInfo = request.getDepartmentHeaderInfo();
            }
            if(!StringUtils.isEmpty(request.getCustomInfo())){
                this.customInfo = request.getCustomInfo();
            }
        }

        public RequestBuilder setGroupLeaderInfo(String groupLeaderInfo) {
            this.groupLeaderInfo = groupLeaderInfo;
            return this;
        }

        public RequestBuilder setManagerInfo(String managerInfo) {
            this.managerInfo = managerInfo;
            return this;
        }

        public RequestBuilder setDepartmentHeaderInfo(String departmentHeaderInfo) {
            this.departmentHeaderInfo = departmentHeaderInfo;
            return this;
        }

        public RequestBuilder setCustomInfo(String customInfo) {
            this.customInfo = customInfo;
            return this;
        }

        public LeaveRequest build() {
            return new LeaveRequest(this);
        }

    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                ", days=" + days +
                ", groupLeaderInfo='" + groupLeaderInfo + '\'' +
                ", managerInfo='" + managerInfo + '\'' +
                ", departmentHeaderInfo='" + departmentHeaderInfo + '\'' +
                ", customInfo='" + customInfo + '\'' +
                '}';
    }
}
