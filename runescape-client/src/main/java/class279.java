import net.runelite.mapping.ObfuscatedName;
import net.runelite.mapping.ObfuscatedSignature;

@ObfuscatedName("js")
public class class279 extends class275 {
   @ObfuscatedName("w")
   @ObfuscatedSignature(
      signature = "Lld;"
   )
   final JagexLoginType field3695;
   @ObfuscatedName("m")
   @ObfuscatedSignature(
      signature = "Lgb;"
   )
   public class206 field3693;

   @ObfuscatedSignature(
      signature = "(Lld;)V"
   )
   public class279(JagexLoginType var1) {
      super(400);
      this.field3693 = new class206();
      this.field3695 = var1;
   }

   @ObfuscatedName("p")
   @ObfuscatedSignature(
      signature = "(B)Lju;",
      garbageValue = "-98"
   )
   class273 vmethod5160() {
      return new class281();
   }

   @ObfuscatedName("i")
   @ObfuscatedSignature(
      signature = "(II)[Lju;",
      garbageValue = "-1319508722"
   )
   class273[] vmethod5161(int var1) {
      return new class281[var1];
   }

   @ObfuscatedName("w")
   @ObfuscatedSignature(
      signature = "(Ljr;ZI)Z",
      garbageValue = "-865953651"
   )
   public boolean method5116(class280 var1, boolean var2) {
      class281 var3 = (class281)this.method5039(var1);
      return var3 == null?false:!var2 || var3.field3705 != 0;
   }

   @ObfuscatedName("q")
   @ObfuscatedSignature(
      signature = "(Lgj;II)V",
      garbageValue = "34396987"
   )
   public void method5117(Buffer var1, int var2) {
      while(true) {
         if(var1.offset < var2) {
            boolean var3 = var1.readUnsignedByte() == 1;
            class280 var4 = new class280(var1.readString(), this.field3695);
            class280 var5 = new class280(var1.readString(), this.field3695);
            int var6 = var1.readUnsignedShort();
            int var7 = var1.readUnsignedByte();
            int var8 = var1.readUnsignedByte();
            boolean var9 = (var8 & 2) != 0;
            boolean var10 = (var8 & 1) != 0;
            if(var6 > 0) {
               var1.readString();
               var1.readUnsignedByte();
               var1.readInt();
            }

            var1.readString();
            if(var4 != null && var4.method5132()) {
               class281 var11 = (class281)this.method5040(var3?var5:var4);
               if(var11 != null) {
                  this.method5042(var11, var4, var5);
                  if(var6 != var11.field3705) {
                     boolean var12 = true;

                     for(class283 var13 = (class283)this.field3693.method3895(); var13 != null; var13 = (class283)this.field3693.method3897()) {
                        if(var13.field3715.equals(var4)) {
                           if(var6 != 0 && var13.field3717 == 0) {
                              var13.method3909();
                              var12 = false;
                           } else if(var6 == 0 && var13.field3717 != 0) {
                              var13.method3909();
                              var12 = false;
                           }
                        }
                     }

                     if(var12) {
                        this.field3693.method3896(new class283(var4, var6));
                     }
                  }
               } else {
                  if(this.method5036() >= 400) {
                     continue;
                  }

                  var11 = (class281)this.method5045(var4, var5);
               }

               var11.field3705 = var6;
               var11.field3707 = var7;
               var11.field3706 = var9;
               var11.field3704 = var10;
               continue;
            }

            throw new IllegalStateException();
         }

         this.method5047();
         return;
      }
   }
}
