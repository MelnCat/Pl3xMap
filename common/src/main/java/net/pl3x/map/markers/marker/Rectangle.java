package net.pl3x.map.markers.marker;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import java.util.Objects;
import net.pl3x.map.JsonObjectWrapper;
import net.pl3x.map.Key;
import net.pl3x.map.markers.Point;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a rectangle marker.
 */
public class Rectangle extends Marker<Rectangle> {
    private Point point1;
    private Point point2;

    private Rectangle(@NotNull Key key) {
        super("rect", key);
    }

    /**
     * Create a new rectangle.
     *
     * @param key identifying key
     * @param x1  first x point
     * @param z1  first z point
     * @param x2  second x point
     * @param z2  second z point
     */
    public Rectangle(@NotNull Key key, double x1, double z1, double x2, double z2) {
        this(key);
        setPoint1(Point.of(x1, z1));
        setPoint2(Point.of(x2, z2));
    }

    /**
     * Create a new rectangle.
     *
     * @param key    identifying key
     * @param point1 first point
     * @param point2 second point
     */
    public Rectangle(@NotNull Key key, @NotNull Point point1, @NotNull Point point2) {
        this(key);
        setPoint1(point1);
        setPoint2(point2);
    }

    /**
     * Create a new rectangle.
     *
     * @param key identifying key
     * @param x1  first x point
     * @param z1  first z point
     * @param x2  second x point
     * @param z2  second z point
     * @return a new rectangle
     */
    public static Rectangle of(@NotNull Key key, double x1, double z1, double x2, double z2) {
        return new Rectangle(key, x1, z1, x2, z2);
    }

    /**
     * Create a new rectangle.
     *
     * @param key    identifying key
     * @param point1 first point
     * @param point2 second point
     * @return a new rectangle
     */
    public static Rectangle of(@NotNull Key key, @NotNull Point point1, @NotNull Point point2) {
        return new Rectangle(key, point1, point2);
    }

    /**
     * Get the first {@link Point} of this rectangle.
     *
     * @return first point
     */
    @NotNull
    public Point getPoint1() {
        return this.point1;
    }

    /**
     * Set the first {@link Point} of this rectangle.
     *
     * @param point1 first point
     * @return this rectangle
     */
    @NotNull
    public Rectangle setPoint1(@NotNull Point point1) {
        Preconditions.checkNotNull(point1, "Rectangle point1 is null");
        this.point1 = point1;
        return this;
    }

    /**
     * Get the second {@link Point} of this rectangle.
     *
     * @return second point
     */
    @NotNull
    public Point getPoint2() {
        return this.point2;
    }

    /**
     * Set the second {@link Point} of this rectangle.
     *
     * @param point2 second point
     * @return this rectangle
     */
    @NotNull
    public Rectangle setPoint2(@NotNull Point point2) {
        Preconditions.checkNotNull(point1, "Rectangle point2 is null");
        this.point2 = point2;
        return this;
    }

    @Override
    @NotNull
    public JsonElement toJson() {
        JsonObjectWrapper wrapper = new JsonObjectWrapper();
        wrapper.addProperty("key", getKey());
        wrapper.addProperty("point1", getPoint1());
        wrapper.addProperty("point2", getPoint2());
        wrapper.addProperty("pane", getPane());
        return wrapper.getJsonObject();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Rectangle other = (Rectangle) o;
        return getKey().equals(other.getKey())
                && getPoint1().equals(other.getPoint1())
                && getPoint2().equals(other.getPoint2())
                && Objects.equals(getPane(), other.getPane())
                && Objects.equals(getOptions(), other.getOptions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getPoint1(), getPoint2(), getPane(), getOptions());
    }

    @Override
    public String toString() {
        return "Rectangle{"
                + "key=" + getKey()
                + ",point1=" + getPoint1()
                + ",point2=" + getPoint2()
                + ",pane=" + getPane()
                + ",options=" + getOptions()
                + "}";
    }
}
