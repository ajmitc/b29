package b29.game.bomber;

import b29.game.crew.CrewMember;

public class Gun {
    private GunPosition position;
    private CrewMember crewMember;
    private int maxAmmo;
    private int ammoRemaining;
    private boolean operational;

    // In-game state
    private boolean fireThisTurn;

    public Gun(GunPosition position, int maxAmmo) {
        this.position = position;
        this.crewMember = null;
        this.maxAmmo = maxAmmo;
        this.ammoRemaining = this.maxAmmo;
        this.operational = true;

        this.fireThisTurn = false;
    }

    public GunPosition getPosition() {
        return position;
    }

    public void setPosition(GunPosition position) {
        this.position = position;
    }

    public CrewMember getCrewMember() {
        return crewMember;
    }

    public void setCrewMember(CrewMember crewMember) {
        this.crewMember = crewMember;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public int getAmmoRemaining() {
        return ammoRemaining;
    }

    public void setAmmoRemaining(int ammoRemaining) {
        this.ammoRemaining = ammoRemaining;
    }

    public void adjAmmoRemaining(int amount) {
        this.ammoRemaining += amount;
        if (this.ammoRemaining < 0)
            this.ammoRemaining = 0;
        if (this.ammoRemaining > maxAmmo)
            this.ammoRemaining = maxAmmo;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    public boolean isFireThisTurn() {
        return fireThisTurn;
    }

    public void setFireThisTurn(boolean fireThisTurn) {
        this.fireThisTurn = fireThisTurn;
    }
}
