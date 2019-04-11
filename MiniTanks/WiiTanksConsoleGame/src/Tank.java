/**
 * class Tank
 * keeps track of the char 'T' movement around the mapArray
 * subclass of Map
 * contains methods shoot and move
 */

public class Tank extends Map{
    /*
    starting position of 'T' char (0,0)
     */
    private int pos_x;
    private int pos_y;

    /**
     *
     * @param width
     * @param length
     * constructor initializes Tank and Map class
     */
    Tank(int width, int length){

        super(width, length);

        pos_x = 0;

        pos_y = 0;
    }

    //move method refers movement to the proper direction
    public void move(String c) {
        if (c.toLowerCase().equals("a")) {
            moveLeft();
        }
        if (c.toLowerCase().equals("s")) {
            moveDown();
        }
        if (c.toLowerCase().equals("d")) {
            moveRight();
        }
        if (c.toLowerCase().equals("w")) {
            moveUp();
        }
    }

    /*moves tank left if valid
     newpos_x/y is the desired new position of the tank
     */

    public void moveLeft() {
        // if tank is on the left boundary it will move to right boundary (opposite side of array)
        if (pos_x == 0) {

            int newpos_x = pos_x + super.getWidth() - 1;

            if (super.ifValidMove(newpos_x, pos_y)) {

                super.changeTankPosition(pos_x, pos_y, newpos_x, pos_y);
                pos_x = newpos_x;
            }
        }
        else {

            int newpos_x = pos_x - 1;

            if (super.ifValidMove(newpos_x, pos_y)) {

                super.changeTankPosition(pos_x, pos_y, newpos_x, pos_y);
                pos_x = newpos_x;
            }
        }
    }


    public void moveRight() {
        // Boundary check
        if (pos_x == super.getWidth() -1) {

            int newpos_x = 0;

            if (super.ifValidMove(newpos_x, pos_y)) {

                super.changeTankPosition(pos_x, pos_y, newpos_x, pos_y);
                pos_x = newpos_x;
            }
        }
        else {
            int newpos_x = pos_x + 1;

            if (super.ifValidMove(newpos_x, pos_y)) {

                super.changeTankPosition(pos_x, pos_y, newpos_x, pos_y);
                pos_x = newpos_x;
            }
        }
    }
    public void moveUp() {
        //Boundary check
        if (pos_y == 0) {

            int newpos_y = pos_y + super.getLength() - 1;

            if (super.ifValidMove(pos_x, newpos_y)) {

                super.changeTankPosition(pos_x, pos_y, pos_x, newpos_y);
                pos_y = newpos_y;
            }
        }
        else {
            int newpos_y = pos_y - 1;

            if (super.ifValidMove(pos_x, newpos_y)) {
                super.changeTankPosition(pos_x, pos_y, pos_x, newpos_y);
                pos_y = newpos_y;
            }
        }
    }
    public void moveDown() {
        //Boundary check
        if (pos_y == super.getLength() - 1) {

            int newpos_y = 0;

            if (super.ifValidMove(pos_x, newpos_y)) {

                super.changeTankPosition(pos_x, pos_y, pos_x, newpos_y);
                pos_y = newpos_y;
            }
        }
        else {
            int newpos_y = pos_y + 1;

            if (super.ifValidMove(pos_x , newpos_y)) {
                super.changeTankPosition(pos_x, pos_y, pos_x, newpos_y);
                pos_y = newpos_y;
            }
        }
    }
    // shoot method refers to proper direction method, depending on user input
    public void shoot(String c) {
        if (c.toLowerCase().equals("a") || c.toLowerCase().equals("d")) {

            shootHor(c);
            }

        else{
            shootVert(c);
        }
    }

    /**
     *
     * @param c
     * parameter determines what direction the bullet is travelling; its change of movement (delta)
     * if shot is valid bullet will keep moving until it hits and object
     * while loop ensures bullet movement is continuous until collision
     */
    public void shootHor(String c) {

        int delta;

        if (c.toLowerCase().equals("a")) {
            //shoot left
            delta = -1;
        } else
            // shoot right
            delta = 1;

        int bulletpos_x = pos_x + delta;

        while (!super.ifValidShot(bulletpos_x - delta, pos_y, bulletpos_x, pos_y)) {

            super.changeBulletPosition(bulletpos_x - delta, pos_y, bulletpos_x, pos_y);

            super.printMapArray();

            bulletpos_x += delta;
            // If bullet hits wall it ricochets
            if (super.getRicochet() == true) {
                delta *= -1;
                bulletpos_x += delta * 2;
            }
        }
    }
    public void shootVert(String c){

        int delta;
        if (c.toLowerCase().equals("w")) {
            //shoot up
            delta = -1;
        }
        else
            // shoot down
            delta = 1;

        int bulletpos_y = pos_y + delta;

        while (!super.ifValidShot(pos_x, bulletpos_y - delta, pos_x, bulletpos_y)) {

            super.changeBulletPosition(pos_x, bulletpos_y - delta, pos_x, bulletpos_y);

            super.printMapArray();

            bulletpos_y += delta;
            // IF bullet hits wall it ricochets
            if (super.getRicochet()) {
                delta *= -1;
                bulletpos_y += delta* 2;
            }
        }
    }

    public int getPos_x() {
        return pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }
}
