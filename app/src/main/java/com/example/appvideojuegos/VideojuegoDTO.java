package com.example.appvideojuegos;

public class VideojuegoDTO {
    private String name;
    private String released;
    private String background_image;
    private int metacritic;

    public VideojuegoDTO(String name, String released, String background_image, int metacritic) {
        this.name = name;
        this.released = released;
        this.background_image = background_image;
        this.metacritic = metacritic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public VideojuegoDTO() {
    }

    public String getBackground() {
        return background_image;
    }

    public void setBackground(String background_image) {
        this.background_image = background_image;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }

    @Override
    public String toString() {
        return "VideojuegoDTO{" +
                "name='" + name + '\'' +
                ", released='" + released + '\'' +
                ", background_image='" + background_image + '\'' +
                ", metacritic=" + metacritic +
                '}';
    }
}
