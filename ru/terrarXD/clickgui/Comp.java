package ru.terrarXD.clickgui;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 18:22
 */
public class Comp {
    public Comp(){

    }
    public void drawScreen(float x, float y, float mouseX, float mouseY){

    }
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button){

    }

    public void init(){

    }

    public void reset(){

    }

    public float getHeight(){
        return 0;
    }

    public float getWidth(){
        return 0;
    }


    public boolean isHover(float x, float y, float x2, float y2, float mouseX, float mouseY){
        return mouseX>x && mouseX < x2 && mouseY>y && mouseY<y2;
    }

}
