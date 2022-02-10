module me.gaestra.sysinforekamedis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.jfoenix;
    requires org.xerial.sqlitejdbc;
    requires java.sql;

    opens me.gaestra.sysinforekamedis to javafx.fxml;
    exports me.gaestra.sysinforekamedis;
    exports me.gaestra.sysinforekamedis.core;
    exports me.gaestra.sysinforekamedis.extras;
    exports me.gaestra.sysinforekamedis.extras.annotations;
    exports me.gaestra.sysinforekamedis.helper;
    exports me.gaestra.sysinforekamedis.model;
}
