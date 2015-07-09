using System;
using Microsoft.SPOT;

namespace iRingGadgeteer
{
    class Patternmatcher
    {
        checkPatterns([int] array)
    {
        if(accX < -30 && accY >200 && accZ < -100)
            {
                accel = accZ;
                SendEventToCallback(MOVEMENT_DOWN);
                Debug.Print("down "+accel);
            }
            if(accX > 100 && accY < -100)
            {
                accel = accZ;
                SendEventToCallback(MOVEMENT_UP);
                Debug.Print("up "+accel);
            }
            if(accX >100 && accY > 50 && accZ < -100)
            {
                accel = accX;
                SendEventToCallback(MOVEMENT_RIGHT);
                Debug.Print("right "+accel);
            }
            if(accX < -100 && accY < -50 && accZ > 30)
            {
                accel = accX;
                SendEventToCallback(MOVEMENT_LEFT);
                Debug.Print("left "+accel);
            }
    }
    }
}
