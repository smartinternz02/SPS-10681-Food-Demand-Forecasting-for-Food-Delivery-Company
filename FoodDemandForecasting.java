
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;
import tech.tablesaw.plotly.api.ScatterPlot;
import tech.tablesaw.plotly.api.VerticalBarPlot;
import static tech.tablesaw.aggregate.AggregateFunctions.*;

import java.io.IOException;

public class FoodDemandForecasting {
   public static void main(String args[]) throws IOException {
       Table train_data = Table.read().csv("C:\\IdeaProjects\\FoodDemandForecasting\\DataSet\\train.csv");
       System.out.println("Train Dataset has "+train_data.shape());
       Table meal_info = Table.read().csv("C:\\IdeaProjects\\FoodDemandForecasting\\DataSet\\meal_info.csv");
       System.out.println("Meal_info Dataset has "+meal_info.shape());
       Table center_info = Table.read().csv("C:\\IdeaProjects\\FoodDemandForecasting\\DataSet\\fulfilment_center_info.csv");
       System.out.println("Center_info Dataset has "+center_info.shape());
       Table first_join = train_data.joinOn("meal_id").inner(meal_info,"meal_id",true);
       Table full_train_data = first_join.joinOn("center_id").inner(center_info,"center_id",true);
       System.out.println(full_train_data.first(5));
       System.out.println(full_train_data.missingValueCounts());
       Table category_orders = full_train_data.select("num_orders","category").summarize("num_orders",sum).by("category").sortDescendingOn("Sum [num_orders]");
       Plot.show(VerticalBarPlot.create("Orders by Categories in Millions by category",category_orders,"category","Sum [num_orders]"));
       Table cuisine_orders = full_train_data.select("num_orders","cuisine").summarize("num_orders",sum).by("cuisine").sortDescendingOn("Sum [num_orders]");
       Plot.show(VerticalBarPlot.create("Orders by Cuisine in Millions",cuisine_orders,"cuisine","Sum [num_orders]"));
       Table region_orders = full_train_data.select("num_orders","region_code").summarize("num_orders",sum).by("region_code").sortDescendingOn("Sum [num_orders]");
       System.out.println(region_orders);
       Plot.show(VerticalBarPlot.create("Number of orders by region code in Millions",region_orders,"region_code","Sum [num_orders]"));
       Table city_orders = full_train_data.select("num_orders","city_code").summarize("num_orders",sum).by("city_code").sortDescendingOn("Sum [num_orders]");
       Plot.show(VerticalBarPlot.create("Number of orders by city code in Millions",city_orders,"city_code","Sum [num_orders]"));
       System.out.println("Number of cities "+city_orders.shape());
       Table center_orders = full_train_data.select("num_orders","center_type").summarize("num_orders",sum).by("center_type").sortDescendingOn("Sum [num_orders]");
       Plot.show(VerticalBarPlot.create("Orders by center type in Millions",center_orders,"center_type","Sum [num_orders]"));

       Plot.show(LinePlot.create("num_orders vs week group by category",full_train_data,"week","num_orders","category"));
       Plot.show(LinePlot.create("num_orders vs week group by cuisine",full_train_data,"week","num_orders","cuisine"));
       Plot.show(LinePlot.create("num_orders vs week group by center_type",full_train_data,"week","num_orders","center_type"));
       Plot.show(LinePlot.create("num_orders vs base_price group by cuisine",full_train_data,"week","base_price","cuisine"));
       Plot.show(ScatterPlot.create("Scatter plot Base Price Vs Checkout Price",full_train_data,"base_price","checkout_price"));
   }
}

