# Commentier 

from digg import *
import getpass 
from math import sqrt
import sys

def mean(list):
	n = len(list)

	if n == 0: return -1
	else: return sum(list) / float(len(list))

def stdDev(list):

	list2 = [ x**2 for x in list]
	
	stdDev = sqrt(mean(list2) - mean(list))

	return stdDev

def analyzeStory(story):

	comments = story.getComments();
	n = int(comments.total)
	print "Total=",n

	while len(comments) < n:
		print "l",len(comments)
		comments += story.getComments(offset=len(comments))
	
	n = len(comments)
	
	print "Number of comments: ", n

	ups = [ int(u.up) for u in comments ]
	downs = [ int(d.down) for d in comments ]
	diggs =  [u - d for u, d in zip(ups, downs)]	

	print "Diggs = ", diggs	

	diggMean = mean(diggs)
	diggStdDev = stdDev(diggs)

	print "mu=",diggMean,"stdDev=",diggStdDev

	# now we need to look in each block and find comments that are 
	

def main():
		
	d = Digg("http://wwww.solidsushi.com") #insert your own application key string

	if len(sys.argv) == 2:
		
		story = d.getStoryByCleanTitle(sys.argv[1])
		print "Story Title:",story[0].title
		analyzeStory(story)

main()

