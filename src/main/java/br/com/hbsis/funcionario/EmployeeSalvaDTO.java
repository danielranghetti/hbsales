package br.com.hbsis.funcionario;

public class EmployeeSalvaDTO {
    private String employeeName;
    private String employeeUuid;

    public EmployeeSalvaDTO() {
    }

    public EmployeeSalvaDTO(String employeeName, String employeeUuid) {
        this.employeeName = employeeName;
        this.employeeUuid = employeeUuid;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }
}
