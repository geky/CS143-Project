/**
 * 
 */
package graphics;

import java.util.List;
import javax.media.opengl.GL2;
import math.Vector3;

public class Polygon {
    private transient Material material;
    List<Vertex> verticies;
    List<String> groups;
    private String materialName;
    public String object;
    public final Vector3 normal;

    public Polygon(String materialName, java.util.Collection<Vertex> verticies) {
        this.materialName = materialName;
        this.verticies = new java.util.ArrayList<Vertex>(verticies);
        this.groups = new java.util.ArrayList<String>();
        
        Vector3 a, b, c;
        a = this.verticies.get(0).coord;
        b = this.verticies.get(1).coord;
        c = this.verticies.get(2).coord;
        this.normal = c.minus(b).cross(a.minus(b)).normalize();
    }
    
    public Polygon(java.util.Collection<Vertex> verticies) {
        this(Material.DEFAULT_MATERIAL, verticies);
    }
    
    public Polygon(Material material, java.util.Collection<Vertex> verticies) {
        this(material.getName(), verticies);
        this.material = material;
    }

    public void render(GL2 gl) {
        if (verticies.size() < 2)
            return;
        
        gl.glColor4f(1.0f, 1.0f, 1.0f,1.0f);
        getMaterial().prepare(gl);
        gl.glBegin(GL2.GL_TRIANGLES);
        if (verticies.size() == 3) {
            for (Vertex v: verticies){
                gl.glTexCoord2f(v.u, v.v); 
                gl.glVertex3f(v.getX(), v.getY(), v.getZ());
            }
        } else {
            Vertex a = verticies.get(0);
            
            for (int i = 2; i < verticies.size(); i++) {
                Vertex b = verticies.get(i - 1);
                Vertex c = verticies.get(i);
                
                gl.glTexCoord2f(a.u, a.v); 
                gl.glVertex3f(a.getX(), a.getY(), a.getZ());
                gl.glTexCoord2f(b.u, b.v); 
                gl.glVertex3f(b.getX(), b.getY(), b.getZ());
                gl.glTexCoord2f(c.u, c.v); 
                gl.glVertex3f(c.getX(), c.getY(), c.getZ());
            }
            
        }
        gl.glEnd();
    }
    
    private Material getMaterial() {
        if(material == null)
            material = Material.findByName(materialName);
        return material;
    }
    
    public Vector3 reflectDirection(Vector3 a) {
        return a.minus(normal.times(2 * a.dotProduct(normal)));
    }
    
    public Vector3 parallelDirection(Vector3 a) {
        return a.minus(normal.times(a.dotProduct(normal)));
    }
    
    public boolean isIntersecting(Vector3 origin, Vector3 direction) {
        Vector3 delta = verticies.get(0).coord.minus(origin);
               
        // Find the time when they intersect
        float t = normal.dotProduct(direction);
        if (t <= 0) // don't divide by zero or intersect with the back of objects
            return false;
        t = normal.dotProduct(delta) / t;
        
        if (t < 0) // We intersected the polygon in the past
            return false;
        
        Vector3 intersection = origin.plus(direction.times(t));
        
        // Walk around the polygon checking the intersection is on the inside of all the edges
        Vector3 last = verticies.get(verticies.size() - 1).coord;
        for (Vertex v: verticies) {
            if (intersection.minus(v.coord).cross(last.minus(v.coord)).dotProduct(normal) < 0)
                return false;
           
            last = v.coord;
        }
        
        return true;
    }
}
