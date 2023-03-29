package edu.uncc.weather;

public class ForecastData {
    String date,temp,max,min,humidity,description,icon;

    @Override
    public String toString() {
        return "ForecastData{" +
                "date='" + date + '\'' +
                ", temp='" + temp + '\'' +
                ", max='" + max + '\'' +
                ", min='" + min + '\'' +
                ", humidity='" + humidity + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ForecastData(String date, String temp, String max, String min, String humidity, String description, String icon) {
        this.date = date;
        this.temp = temp;
        this.max = max;
        this.min = min;
        this.humidity = humidity;
        this.description = description;
        this.icon = icon;
    }
}
