//Kelas utama Mutation
public interface Mutation {
    //Return void: hanya menerima dan memodifikasi objek Chromosome secara langsung
    public void mutate(Chromosome chromosome);
}