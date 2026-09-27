package cn.handyplus.guild.hook;

import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.entity.MyPet;
import org.bukkit.entity.Player;

public class MyPetV3Util {
    private MyPetV3Util() {
    }

    public static void removeMyPet(Player player) {
        MyPet myPet = MyPetApi.getMyPetManager().getMyPet(player);
        if (myPet != null) {
            myPet.setStatus(MyPet.PetState.Despawned);
        }
    }
}
