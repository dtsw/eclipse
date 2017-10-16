package org.datanucleus.samples.jdo.tutorial;

import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

public class QPerson extends org.datanucleus.samples.jdo.tutorial.QSubscriber
{
    public static final QPerson jdoCandidate = candidate("this");

    public static QPerson candidate(String name)
    {
        return new QPerson(null, name, 5);
    }

    public static QPerson candidate()
    {
        return jdoCandidate;
    }

    public static QPerson parameter(String name)
    {
        return new QPerson(Person.class, name, ExpressionType.PARAMETER);
    }

    public static QPerson variable(String name)
    {
        return new QPerson(Person.class, name, ExpressionType.VARIABLE);
    }

    public final StringExpression firstname;
    public final StringExpression lastname;
    public final StringExpression username;
    public final CollectionExpression devices;

    public QPerson(PersistableExpression parent, String name, int depth)
    {
        super(parent, name, depth);
        this.firstname = new StringExpressionImpl(this, "firstname");
        this.lastname = new StringExpressionImpl(this, "lastname");
        this.username = new StringExpressionImpl(this, "username");
        this.devices = new CollectionExpressionImpl(this, "devices");
    }

    public QPerson(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.firstname = new StringExpressionImpl(this, "firstname");
        this.lastname = new StringExpressionImpl(this, "lastname");
        this.username = new StringExpressionImpl(this, "username");
        this.devices = new CollectionExpressionImpl(this, "devices");
    }
}
