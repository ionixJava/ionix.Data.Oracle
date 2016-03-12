package ionix.Data.Oracle.Conversion;


import ionix.Conversion.ByteArrayConvertible;
import oracle.sql.BLOB;
import sun.misc.BASE64Encoder;


public class BlobConvertible extends ByteArrayConvertible {

    @Override
    public String toString(Object value) {
        BLOB bvalue = (BLOB)value;
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bvalue.getBytes());
    }


    @Override
    public byte[] toByteArray(Object value) {
        BLOB bvalue = (BLOB)value;
        return bvalue.getBytes();
    }
}
