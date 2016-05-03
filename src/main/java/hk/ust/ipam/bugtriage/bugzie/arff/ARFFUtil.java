package hk.ust.ipam.bugtriage.bugzie.arff;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Utility static methods
 * Created by jeehoonyoo on 8/8/14.
 */
public final class ARFFUtil {
    /**
     * Reads ARFF files
     * @param filename  ARFF file path
     * @param idxClass  The index of class attribute
     * @return  Read instances
     */
    public static Instances readData(String filename, int idxClass)
    {
        DataSource source;
        try {
            source = new DataSource(filename);
        } catch (Exception e) {
            System.out.println("Cannot read data file: " + e.toString());
            return null;
        }

        return readData(source, idxClass);
    }

    /**
     * Reads ARFF files
     * @param filename  ARFF file path
     * @param className  The name of class attribute
     * @return  Read instances
     */
    public static Instances readData(String filename, String className)
    {
        DataSource source;
        try {
            source = new DataSource(filename);
        } catch (Exception e) {
            System.out.println("Cannot read data file: " + e.toString());
            return null;
        }

        return readData(source, className);
    }

    /**
     * Reads ARFF stream
     * @param fileStream    ARFF file stream
     * @param idxClass  The index of class attribute
     * @return  Read instances
     */
    public static Instances readData(InputStream fileStream, int idxClass)
    {
        DataSource source = new DataSource(fileStream);
        return readData(source, idxClass);
    }

    /**
     * Reads ARFF file from DataSource. For detailed information, please check DataSource Weka document
     * @param source    ARFF file in DataSource format
     * @param idxClass  The index of class attribute
     * @return  Read instances
     */
    private static Instances readData(DataSource source, int idxClass)
    {
        Instances data;

        try {
            data = source.getDataSet();
        } catch (Exception e) {
            System.out.println("Cannot read data file: " + e.toString());
            return null;
        }

        if (data == null) {
            System.out.println("Cannot load data.");
            return null;
        }

        if (idxClass < 0) {
            System.out.println("The class is automatically set to the last attribute");
            idxClass = data.numAttributes() - 1;
        }

        data.setClassIndex(idxClass);
        return data;
    }

    /**
     * Reads ARFF file from DataSource. For detailed information, please check DataSource Weka document
     * @param source    ARFF file in DataSource format
     * @param className  The name of class attribute
     * @return  Read instances
     */
    private static Instances readData(DataSource source, String className)
    {
        Instances data;

        try {
            data = source.getDataSet();
        } catch (Exception e) {
            System.out.println("Cannot read data file: " + e.toString());
            return null;
        }

        if (data == null) {
            System.out.println("Cannot load data.");
            return null;
        }

        Attribute classAttribute = null;
        if (className != null)
            classAttribute = data.attribute(className);

        if (classAttribute != null) {
            data.setClass(classAttribute);
        } else {
            System.out.println("The class is automatically set to the last attribute");
            data.setClassIndex(data.numAttributes() - 1);
        }
        return data;
    }

    /**
     * Writes ARFF file with data.
     * @param data      Instances to be written.
     * @param filename  ARFF file path
     */
    public static void writeData(Instances data, String filename) {
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File(filename));
            saver.writeBatch();
        } catch (Exception e) {
            System.out.println("Cannot write data file: " + e.toString());
        }
    }

    /**
     * Counts the number of target instances given class index
     * @param data  Target instances
     * @param idxTargetClass    The index of target class
     * @return  The number of target instances
     */
    public static int countInstances(Instances data, int idxTargetClass) {
        if (data.classIndex() < 0) {
            System.out.println("class attribute is not defined");
            return ARFFClassCode.NO_CLASS_DEFINED;
        }

        int noTargets = 0;
        for (int i = 0; i < data.numInstances(); i++) {
            if (idxTargetClass == (int)data.get(i).classValue())
                noTargets++;
        }

        return noTargets;
    }

    /**
     * Counts the number of target instances given class name
     * @param data  Target instances
     * @param targetClassName   The name of target class
     * @return  The number of target instances
     */
    public static int countInstances(Instances data, String targetClassName) {
        if (data.classIndex() < 0) {
            System.out.println("class attribute is not defined");
            return ARFFClassCode.NO_CLASS_DEFINED;
        }

        int idxTargetClass = findTargetClassIdx(data.classAttribute(), targetClassName);
        if (idxTargetClass == ARFFClassCode.NO_CLASS)
            return ARFFClassCode.NO_CLASS;

        return countInstances(data, idxTargetClass);
    }

    /**
     * Finds the index of class in class attribute by name
     * @param data    Target instances
     * @param className The name of class
     * @return  The index of class
     */
    public static int findTargetClassIdx(Instances data, String className) {
        Attribute classAttribute = data.classAttribute();
        if (classAttribute == null) {
            System.out.println("cannot find the class by given class name");
            return ARFFClassCode.NO_CLASS;
        }

        return findTargetClassIdx(classAttribute, className);
    }

    /**
     * Finds the index of class in class attribute by name
     * @param classAttribute    Class attribute from instances
     * @param className The name of class
     * @return  The index of class
     */
    public static int findTargetClassIdx(Attribute classAttribute, String className) {
        if (classAttribute == null || className == null) {
            System.out.println("cannot find the class by given class name");
            return ARFFClassCode.NO_CLASS;
        }

        int idx = classAttribute.indexOfValue(className);

        if (idx == -1) {
            System.out.println("cannot find the class by given class name");
            return ARFFClassCode.NO_CLASS;
        }
        else
            return idx;
    }

    /**
     * Excludes some attributes by filter
     * @param removingIdx   The array of indexes of attributes to be excluded
     * @return  Remove object
     */
    public static Remove removeIdx(int[] removingIdx) {
        if (removingIdx == null) {
            System.out.println("removing indexes are not set");
            return null;
        }

        Remove rm = new Remove();
        rm.setAttributeIndicesArray(removingIdx);

        return rm;
    }
}
