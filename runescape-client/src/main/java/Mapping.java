import net.runelite.mapping.Export;
import net.runelite.mapping.Implements;
import net.runelite.mapping.ObfuscatedName;

@ObfuscatedName("da")
@Implements("Mapping")
public class Mapping {
   @ObfuscatedName("d")
   int field1645;
   @ObfuscatedName("z")
   @Export("mux")
   int mux;
   @ObfuscatedName("n")
   @Export("submapFloors")
   int[] submapFloors;
   @ObfuscatedName("r")
   int[] field1643;

   Mapping() {
      class104.getInt(16);
      this.field1645 = class104.getBit() != 0?class104.getInt(4) + 1:1;
      if(class104.getBit() != 0) {
         class104.getInt(8);
      }

      class104.getInt(2);
      if(this.field1645 > 1) {
         this.mux = class104.getInt(4);
      }

      this.submapFloors = new int[this.field1645];
      this.field1643 = new int[this.field1645];

      for(int var1 = 0; var1 < this.field1645; ++var1) {
         class104.getInt(8);
         this.submapFloors[var1] = class104.getInt(8);
         this.field1643[var1] = class104.getInt(8);
      }

   }
}
