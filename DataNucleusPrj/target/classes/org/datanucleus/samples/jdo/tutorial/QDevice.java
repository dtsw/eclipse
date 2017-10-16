package org.datanucleus.samples.jdo.tutorial;

import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

public class QDevice extends org.datanucleus.samples.jdo.tutorial.QSubscriber
{
    public static final QDevice jdoCandidate = candidate("this");

    public static QDevice candidate(String name)
    {
        return new QDevice(null, name, 5);
    }

    public static QDevice candidate()
    {
        return jdoCandidate;
    }

    public static QDevice parameter(String name)
    {
        return new QDevice(Device.class, name, ExpressionType.PARAMETER);
    }

    public static QDevice variable(String name)
    {
        return new QDevice(Device.class, name, ExpressionType.VARIABLE);
    }

    public final StringExpression type;
    public final StringExpression address;

    public QDevice(PersistableExpression parent, String name, int depth)
    {
        super(parent, name, depth);
        this.type = new StringExpressionImpl(this, "type");
        this.address = new StringExpressionImpl(this, "address");
    }

    public QDevice(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.type = new StringExpressionImpl(this, "type");
        this.address = new StringExpressionImpl(this, "address");
    }
}
