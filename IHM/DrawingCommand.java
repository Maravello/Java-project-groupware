import java.io.Serializable;

/**
 * Représente une commande de dessin à synchroniser entre clients.
 * Peut être sérialisée pour transmission réseau.
 */
public class DrawingCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type {
        LINE,      // Ligne
        RECT,      // Rectangle
        CIRCLE,    // Cercle
        CLEAR,     // Effacer tout
        FREEHAND   // Dessin libre
    }
    
    private Type type;
    private int x1, y1, x2, y2;
    private int color;
    private String clientId;
    
    public DrawingCommand(Type type, int x1, int y1, int x2, int y2, int color, String clientId) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.clientId = clientId;
    }
    
    public DrawingCommand(Type type, String clientId) {
        this.type = type;
        this.clientId = clientId;
    }
    
    // Getters
    public Type getType() { return type; }
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
    public int getColor() { return color; }
    public String getClientId() { return clientId; }
    
    @Override
    public String toString() {
        return "DrawingCommand{" + "type=" + type + ", x1=" + x1 + ", y1=" + y1 + 
               ", x2=" + x2 + ", y2=" + y2 + ", color=" + color + ", clientId=" + clientId + '}';
    }
}
