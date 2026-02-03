package utils;

public enum HeaderMenuItem {
    HOME("//a[text()='HOME']"),
    ABOUT("//a[text()='ABOUT']"),
    CONTACTS("//a[text()='CONTACTS']"),
    ADD("//a[text()='ADD']"),
    LOGIN("//a[text()='LOGIN']"),
    SIGN_OUT("//button[text()='Sign Out']");

    private final String locator;

    HeaderMenuItem(String locator) {
        this.locator = locator;
    }
    public String getLocator(){
        return locator;
    }
}
