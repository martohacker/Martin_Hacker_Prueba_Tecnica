package com.martinhacker.jsonplaceholder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de Usuario")
public class User {
    
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Nombre del usuario", example = "Leanne Graham")
    private String name;
    
    @Schema(description = "Nombre de usuario", example = "Bret")
    private String username;
    
    @Schema(description = "Email del usuario", example = "Sincere@april.biz")
    private String email;
    
    @Schema(description = "Información de contacto del usuario")
    private Address address;
    
    @Schema(description = "Teléfono del usuario", example = "1-770-736-8031 x56442")
    private String phone;
    
    @Schema(description = "Sitio web del usuario", example = "hildegard.org")
    private String website;
    
    @Schema(description = "Información de la empresa del usuario")
    private Company company;
    
    // Constructores
    public User() {}
    
    public User(Long id, String name, String username, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
    // Clases internas para Address y Company
    @Schema(description = "Dirección del usuario")
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;
        
        // Constructores, getters y setters
        public Address() {}
        
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getSuite() { return suite; }
        public void setSuite(String suite) { this.suite = suite; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getZipcode() { return zipcode; }
        public void setZipcode(String zipcode) { this.zipcode = zipcode; }
        
        public Geo getGeo() { return geo; }
        public void setGeo(Geo geo) { this.geo = geo; }
        
        @Schema(description = "Coordenadas geográficas")
        public static class Geo {
            private String lat;
            private String lng;
            
            public Geo() {}
            
            public String getLat() { return lat; }
            public void setLat(String lat) { this.lat = lat; }
            
            public String getLng() { return lng; }
            public void setLng(String lng) { this.lng = lng; }
        }
    }
    
    @Schema(description = "Información de la empresa")
    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;
        
        public Company() {}
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getCatchPhrase() { return catchPhrase; }
        public void setCatchPhrase(String catchPhrase) { this.catchPhrase = catchPhrase; }
        
        public String getBs() { return bs; }
        public void setBs(String bs) { this.bs = bs; }
    }
}
