htmlgen
=======

Write browser-friendly output for various data structures.

# Status

Data structures
- [x] Basic Types
- [x] Iterables
- [x] Maps
- [x] Product and Case classes
- [ ] Line Plots
- [ ] Histograms
- [ ] Pie charts
- [ ] Graphs
- [ ] Sequence Tagging

Converters
  * [x] toString
  * [x] Tables for everything
  * [x] Lists for everything
  * [x] `div` and `span` based
  * [ ] D3js/Canvas based

## Examples

For more examples, check out the html files in `src/main/resources/`.

Consider the following Scala snippet:

```scala
    case class Company(name: String, address: String)
    case class Person(first: String, last: String, email: String, age: Int, employedBy: Company)

    val c1 = Company("Battle School", "Seattle, WA")
    val c2 = Company("Command School", "Mountain View, CA")
    val c3 = Company("Dept of Xenobiology", "Lusitania")
    val p1 = Person("Andrew", "Wiggin", "ender@intfleet.com", 10, c2)
    val p2 = Person("Hyrum", "Graff", "graff@intfleet.com", 65, c2)
    val p3 = Person("Ivanova", "von Hesse", "novinha@xenbio.com", 25, c3)
    
    Map(c2 -> List(p1, p2), c3 -> List(p3)
```

### toString

A simple "toString" looks like the following:

```
Map(Company(Command School,Mountain View, CA) -> List(Person(Andrew,Wiggin,ender@intfleet.com,10,Company(Command School,Mountain View, CA)), Person(Hyrum,Graff,graff@intfleet.com,65,Company(Command School,Mountain View, CA))), Company(Dept of Xenobiology,Lusitania) -> List(Person(Ivanova,von Hesse,novinha@xenbio.com,25,Company(Dept of Xenobiology,Lusitania))))
```

### Tables

Using `TableConverter.convert()` results in:

<table>
<tbody><tr><td colspan="2" class="property">Map</td></tr>
  <tr><td class="property">    <table>
<tbody><tr><td colspan="2" class="property">Company</td></tr>
      <tr><td class="property">name</td><td>        <code>Command School</code>
      </td></tr>
      <tr><td class="property">address</td><td>        <code>Mountain View, CA</code>
      </td></tr>
    </tbody></table>
</td><td>    <table>
<tbody><tr><td colspan="2" class="property">List</td></tr>
      <tr><td class="property">        <code>[0]</code>
</td><td>        <table>
<tbody><tr><td colspan="2" class="property">Person</td></tr>
          <tr><td class="property">first</td><td>            <code>Andrew</code>
          </td></tr>
          <tr><td class="property">last</td><td>            <code>Wiggin</code>
          </td></tr>
          <tr><td class="property">email</td><td>            <code>ender@intfleet.com</code>
          </td></tr>
          <tr><td class="property">age</td><td>            <code>10</code>
          </td></tr>
          <tr><td class="property">employedBy</td><td>            <table>
<tbody><tr><td colspan="2" class="property">Company</td></tr>
              <tr><td class="property">name</td><td>                <code>Command School</code>
              </td></tr>
              <tr><td class="property">address</td><td>                <code>Mountain View, CA</code>
              </td></tr>
            </tbody></table>
          </td></tr>
        </tbody></table>
</td></tr>
      <tr><td class="property">        <code>[1]</code>
</td><td>        <table>
<tbody><tr><td colspan="2" class="property">Person</td></tr>
          <tr><td class="property">first</td><td>            <code>Hyrum</code>
          </td></tr>
          <tr><td class="property">last</td><td>            <code>Graff</code>
          </td></tr>
          <tr><td class="property">email</td><td>            <code>graff@intfleet.com</code>
          </td></tr>
          <tr><td class="property">age</td><td>            <code>65</code>
          </td></tr>
          <tr><td class="property">employedBy</td><td>            <table>
<tbody><tr><td colspan="2" class="property">Company</td></tr>
              <tr><td class="property">name</td><td>                <code>Command School</code>
              </td></tr>
              <tr><td class="property">address</td><td>                <code>Mountain View, CA</code>
              </td></tr>
            </tbody></table>
          </td></tr>
        </tbody></table>
</td></tr>
    </tbody></table>
</td></tr>
  <tr><td class="property">    <table>
<tbody><tr><td colspan="2" class="property">Company</td></tr>
      <tr><td class="property">name</td><td>        <code>Dept of Xenobiology</code>
      </td></tr>
      <tr><td class="property">address</td><td>        <code>Lusitania</code>
      </td></tr>
    </tbody></table>
</td><td>    <table>
<tbody><tr><td colspan="2" class="property">List</td></tr>
      <tr><td class="property">        <code>[0]</code>
</td><td>        <table>
<tbody><tr><td colspan="2" class="property">Person</td></tr>
          <tr><td class="property">first</td><td>            <code>Ivanova</code>
          </td></tr>
          <tr><td class="property">last</td><td>            <code>von Hesse</code>
          </td></tr>
          <tr><td class="property">email</td><td>            <code>novinha@xenbio.com</code>
          </td></tr>
          <tr><td class="property">age</td><td>            <code>25</code>
          </td></tr>
          <tr><td class="property">employedBy</td><td>            <table>
<tbody><tr><td colspan="2" class="property">Company</td></tr>
              <tr><td class="property">name</td><td>                <code>Dept of Xenobiology</code>
              </td></tr>
              <tr><td class="property">address</td><td>                <code>Lusitania</code>
              </td></tr>
            </tbody></table>
          </td></tr>
        </tbody></table>
</td></tr>
    </tbody></table>
</td></tr>
</tbody></table>

### Lists

While using `ListConverter.convert()` results in:

<div><span class="property">Map</span><ul>
  <li><span class="property"><span class="property">Company</span>    <ul>
      <li><span class="property">name</span>:         <code>Command School</code>
</li>
      <li><span class="property">address</span>:         <code>Mountain View, CA</code>
</li>
    </ul>
</span> → <span><span class="property">List</span>    <ol start="0">
      <li><span class="property">Person</span>        <ul>
          <li><span class="property">first</span>:             <code>Andrew</code>
</li>
          <li><span class="property">last</span>:             <code>Wiggin</code>
</li>
          <li><span class="property">email</span>:             <code>ender@intfleet.com</code>
</li>
          <li><span class="property">age</span>:             <code>10</code>
</li>
          <li><span class="property">employedBy</span>: <span class="property">Company</span>            <ul>
              <li><span class="property">name</span>:                 <code>Command School</code>
</li>
              <li><span class="property">address</span>:                 <code>Mountain View, CA</code>
</li>
            </ul>
</li>
        </ul>
</li>      <li><span class="property">Person</span>        <ul>
          <li><span class="property">first</span>:             <code>Hyrum</code>
</li>
          <li><span class="property">last</span>:             <code>Graff</code>
</li>
          <li><span class="property">email</span>:             <code>graff@intfleet.com</code>
</li>
          <li><span class="property">age</span>:             <code>65</code>
</li>
          <li><span class="property">employedBy</span>: <span class="property">Company</span>            <ul>
              <li><span class="property">name</span>:                 <code>Command School</code>
</li>
              <li><span class="property">address</span>:                 <code>Mountain View, CA</code>
</li>
            </ul>
</li>
        </ul>
</li>    </ol>
</span></li>
  <li><span class="property"><span class="property">Company</span>    <ul>
      <li><span class="property">name</span>:         <code>Dept of Xenobiology</code>
</li>
      <li><span class="property">address</span>:         <code>Lusitania</code>
</li>
    </ul>
</span> → <span><span class="property">List</span>    <ol start="0">
      <li><span class="property">Person</span>        <ul>
          <li><span class="property">first</span>:             <code>Ivanova</code>
</li>
          <li><span class="property">last</span>:             <code>von Hesse</code>
</li>
          <li><span class="property">email</span>:             <code>novinha@xenbio.com</code>
</li>
          <li><span class="property">age</span>:             <code>25</code>
</li>
          <li><span class="property">employedBy</span>: <span class="property">Company</span>            <ul>
              <li><span class="property">name</span>:                 <code>Dept of Xenobiology</code>
</li>
              <li><span class="property">address</span>:                 <code>Lusitania</code>
</li>
            </ul>
</li>
        </ul>
</li>    </ol>
</span></li>
</ul>
</div>
