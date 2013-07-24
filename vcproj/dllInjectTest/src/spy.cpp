#include "spy.h"
#include "Session.h"

void Run()
{
	char c;
	while (true)
	{
		scanf("%c", &c);
		switch (c)
		{
		case 'y':
			{
				CSession<unsigned> session;
				unsigned value;
				do 
				{
					scanf("%d", &value);

				} while (session.Search(value)>10);
												
			}
			break;
		case 'n':
			break;
		default:
			return ;
			break;
		}
	}
	
	
}