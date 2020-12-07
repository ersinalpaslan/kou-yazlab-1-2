package mantik;

public class Kontrol extends Thread
{
	public void kontrolEt()
	{
		int bekleyenKisiSayisi = 0;

		synchronized (AVM.katlar)
		{

			for (int i = 0; i < 5; i++)
			{
				if (i == 0) // zemin
				{
					bekleyenKisiSayisi += AVM.katlar[0].getMusteriler().size();
				}
				else
				{
					for (Musteri musteri : AVM.katlar[i].getMusteriler())
					{
						if (musteri.cikiyormu)
						{
							bekleyenKisiSayisi += 1;
						}
					}
				}
			}
		}

		synchronized (AVM.asansorler)
		{
			int aktifAsansorSayisi = 0;

			for (Asansor asansor : AVM.asansorler)
			{
				if (asansor.calisiyor)
				{
					aktifAsansorSayisi += 1;
				}
			}

			// asansör gerekiyorsa
			if (bekleyenKisiSayisi > 20) // aktifAsansorSayisi * 10 * 2
			{
				/*for (Asansor asansor : AVM.asansorler)
				{
					if (!asansor.isAlive() && !asansor.calisiyor)
					{
						asansor.start();
						break;
					}
				}*/

				for (int i = 0; i < AVM.asansorler.length; i++)
				{
					Asansor asansor = AVM.asansorler[i];
					if (!asansor.isAlive() && !asansor.calisiyor)
					{
						AVM.asansorler[i] = new Asansor();
						AVM.asansorler[i].start();
						break;
					}
				}
			}
			else if(aktifAsansorSayisi>1) // if(bekleyenKisiSayisi <= 20) // gereksiz asansör varsa (aktifAsansorSayisi - 1) * 10 * 2
			{
				for (Asansor asansor : AVM.asansorler)
				{
					if (asansor.isAlive() && asansor.calisiyor)
					{
						// TODO: durmadan önce içindekileri indir istedikleri kata
						asansor.durdur();
						break;
					}
				}
			}
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			kontrolEt();
		}
	}
}
