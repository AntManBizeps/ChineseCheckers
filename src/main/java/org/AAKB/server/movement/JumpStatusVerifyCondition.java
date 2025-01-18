package org.AAKB.server.movement;

public class JumpStatusVerifyCondition implements AdditionalVerifyCondition{
    private int status;

    public JumpStatusVerifyCondition(int status)
    {
        this.status = status;
    }

    /**
     * weryfikuje jaki ruch został wykonany. Zwraca false jeśli był to ruch przeskakujący (wykorzystanie w verifyMove)
     */
    public boolean verify()
    {
        return status != 2;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
