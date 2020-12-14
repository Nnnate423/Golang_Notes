package main
import (
	"fmt"
)

func bubble(arr []int){
	for i:=0;i<cap(arr)-1;i++{
		for j:=0;j<cap(arr)-i-1;j++{
			if arr[j]>arr[j+1] {
				arr[j],arr[j+1]=arr[j+1],arr[j]
			}
		}
	}
}

func main() {
	array:=[]int{3,4,1,5,7,2,10,6,9,10}
	bubble(array)
	for i,num := range array{
		fmt.Println(i,num)
	}
}
